package com.qxc.utiles.containertools;

import com.qxc.pluginunit.PlugInUnitDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author qxc
 * @version 1.0
 * @date 2023 2023/7/6 16:05
 * @PACKAGE com.qxc.utiles
 * @description 定长线程安全容器
 * @param <T>
 */
@Slf4j
public class FixArrayListImpl<T extends PlugInUnitDomain> implements FixArrayList<T> {

    @Getter
    @NoArgsConstructor
    private final static class Container {
        private final ArrayList<PlugInUnitDomain> vector = new ArrayList<>();
        private final ArrayList<PlugInUnitDomain> tVector = new ArrayList<>();
        private final HashSet<String> version = new HashSet<>();

        public void add(String version, PlugInUnitDomain obj) {
            this.version.add(version);
            vector.add(obj);
        }

        public void add(String version, PlugInUnitDomain obj, int index) {
            this.version.add(version);
            vector.add(index, obj);
        }

        @Contract(pure = true)
        public boolean containsV(String version) {
            for (PlugInUnitDomain pd : vector) {
                if (pd.getVersion().equals(version)) {
                    return true;
                }
            }
            return false;
        }

        @Contract(pure = true)
        public boolean contains(String version) {
            return this.version.contains(version);
        }

        /**
         * 在缓存区与活动区中对对象进行查找
         *
         */
        public @Nullable PlugInUnitDomain get(String version, boolean fromAll) {
            for (PlugInUnitDomain pd : vector) {
                if (pd.getVersion().equals(version)) {
                    return pd;
                }
            }
            if (!fromAll) {
                return null;
            }
            for (PlugInUnitDomain pd : tVector) {
                if (pd.getVersion().equals(version)) {
                    return pd;
                }
            }
            return null;
        }

        @Contract(pure = true)
        public @Nullable PlugInUnitDomain getLatest() {
            return vector.size() == 0 ? null : vector.get(vector.size() - 1);
        }
    }

    private final Container container = new Container();
    private final static TimeUnit TIMEUNIT = TimeUnit.MILLISECONDS;

    private final RefuseStrategies<T> refuseStrategies;
    private final String baseName;
    private final int capacity;
    private int currentSize = 0;
    private int currentTmpSize = 0;
    private final int tempSize;
    private PlugInUnitDomain curObj;
    private final long lockTime;

    private final Lock rLock;
    private final Lock wLock;

    /**
     * 读写锁分离
     * @param capacity
     * @param tempSize
     * @param lockTime
     * @param baseName
     * @param refuseStrategies
     */
    public FixArrayListImpl(int capacity, int tempSize, long lockTime, String baseName, RefuseStrategies<T> refuseStrategies) {
        this.capacity = capacity;
        this.tempSize = tempSize;
        this.refuseStrategies = refuseStrategies;
        this.curObj = null;
        this.lockTime = lockTime;
        this.baseName = baseName;
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        rLock = lock.readLock();
        wLock = lock.writeLock();
    }

    @SuppressWarnings("MissingJavadoc")
    public FixArrayListImpl(int capacity, int tempSize, String baseName, RefuseStrategies<T> refuseStrategies) {
        this(capacity, tempSize, 1000, baseName, refuseStrategies);
    }

    @SuppressWarnings("MissingJavadoc")
    public FixArrayListImpl(int capacity, String baseName, RefuseStrategies<T> refuseStrategies) {
        this(capacity, 4, baseName, refuseStrategies);
    }

    @SuppressWarnings("MissingJavadoc")
    public FixArrayListImpl(String baseName, RefuseStrategies<T> refuseStrategies) {
        this(3, baseName, refuseStrategies);
    }

    /**
     * 添加T对象使用
     *
     */
    @Override
    public boolean add(String version, PlugInUnitDomain obj) {
        return add(version, obj, currentSize);
    }

    /**
     * 在指定位置处添加T对象并使用默认名称
     *
     */
    @Override
    public boolean add(String version, PlugInUnitDomain obj, int index) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 尝试进行添加，首先查询是否有重复对象
                    if (conatins(version, true) || conatins(obj, true)) {
                        log.info(baseName + " object repeat!!");
                        return false;
                    }
                    // 查询位置是否充足
                    if (!isFull()) {
                        container.add(version, obj, index);
                        currentSize += 1;
                        return true;
                    }
                    // 查询缓存列表是否已满
                    if (currentTmpSize == tempSize) {
                        moveToSt();
                    }
                    container.tVector.add(container.vector.get(0));
                    currentTmpSize++;
                    container.vector.remove(0);
                    container.add(version, obj, index);
                    return true;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " add object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " add object has been interrupted!!");
            return false;
        }
    }

    /**
     * 删除T对象
     *
     */
    @Override
    public boolean remove(PlugInUnitDomain obj) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 查找
                    if (!conatins(obj, true)) {
                        log.info(baseName + " no such object!!");
                        return false;
                    }
                    // 进行删除
                    if (container.vector.remove(obj)) {
                        currentSize--;
                        container.version.remove(obj.getVersion());
                        return true;
                    }
                    return false;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " remove object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " remove object has been interrupted!!");
            return false;
        }
    }

    /**
     * 删除对应位置出的对象
     *
     */
    @Override
    public boolean remove(int index) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 查找
                    if (index >= currentSize && index < 0) {
                        log.info(baseName + " no such object!!");
                        return false;
                    }
                    container.version.remove(container.vector.get(index).getVersion());
                    // 进行删除
                    container.vector.remove(index);
                    currentSize--;
                    return true;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " remove object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " remove object has been interrupted!!");
            return false;
        }
    }

    /**
     * 删除指定名称的对象
     *
     */
    @Override
    public boolean remove(String version) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 查找
                    if (!conatins(version, true)) {
                        log.info(baseName + " no such object!!");
                        return false;
                    }
                    final PlugInUnitDomain plugInUnitDomain = container.get(version, false);
                    if (plugInUnitDomain == null) {
                        return false;
                    }
                    container.vector.remove(plugInUnitDomain);
                    currentSize--;
                    container.version.remove(version);
                    return true;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " remove object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " remove object has been interrupted!!");
            return false;
        }
    }

    /**
     * 更新index处的对象
     *
     */
    @Override
    public boolean update(int index, PlugInUnitDomain obj) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 查找
                    if (!conatins(index)) {
                        log.warn(baseName + " update object not found!!");
                        return false;
                    }
                    obj.setVersion(container.vector.get(index).getVersion());
                    container.vector.set(index, obj);
                    return true;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " update object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " update object has been interrupted!!");
            return false;
        }
    }

    /**
     * 更新名称为version的对象
     *
     */
    @Override
    public boolean update(String version, PlugInUnitDomain obj) {
        try {
            if (wLock.tryLock(lockTime, TIMEUNIT)) {
                // 获得锁
                try {
                    // 查找全部区域有没有这个值
                    if (!conatins(version, true)) {
                        log.warn(baseName + " update object not found!!");
                        return false;
                    }
                    for (int i = 0; i < container.vector.size(); i++) {
                        if (container.vector.get(i).getVersion().equals(version)) {
                            container.vector.set(i, obj);
                            return true;
                        }
                    }
                    for (int i = 0; i < container.tVector.size(); i++) {
                        if (container.tVector.get(i).getVersion().equals(version)) {
                            container.tVector.set(i, obj);
                            return true;
                        }
                    }
                    return false;
                } finally {
                    // 更新当前的最新版本
                    curObj = container.getLatest();
                    wLock.unlock();
                }
            } else {
                log.warn(baseName + " update object time out!!");
                return false;
            }
        } catch (InterruptedException e) {
            log.warn(baseName + " update object has been interrupted!!");
            return false;
        }
    }

    /**
     * 查找对应的对象
     *
     */
    @Override
    public boolean conatins(@NotNull PlugInUnitDomain obj, boolean fromAll) {
        rLock.lock();
        try {
            return conatins(obj.getVersion(), fromAll);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 查找对象
     *
     */
    @Override
    public boolean conatins(String version, boolean fromAll) {
        rLock.lock();
        try {
            return container.get(version, fromAll) != null;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 查找对象
     *
     */
    @Override
    public boolean conatins(int index) {
        rLock.lock();
        try {
            return currentSize > index;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取对象,可以获取活动区与缓存区域的全部对象
     *
     */
    @Override
    public T getObject(String version) {
        rLock.lock();
        try {
            if (conatins(version, true)) {
                return (T) container.get(version, true);
            }
            return null;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取当前活动区域的对象
     *
     */
    @Override
    public T getObject(int index) {
        rLock.lock();
        try {
            if (currentSize < index) {
                return null;
            }
            return (T) container.vector.get(index);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 当前对象获取
     *
     */
    @Override
    public T getCurObject() {
        return (T) curObj;
    }

    /**
     * 得到当前的剩余容量
     *
     */
    @Override
    public int getLeaveSize() {
        return capacity - currentSize;
    }

    /**
     * 得到当前已经使用的容量
     *
     */
    @Override
    public int getSize() {
        return currentSize;
    }

    /**
     * 得到容量
     *
     */
    @Override
    public int getCapacity() {
        return capacity;
    }


    /**
     * 是否已经满了
     *
     */
    @Override
    public boolean isFull() {
        return currentSize >= capacity;
    }

    /**
     * 获取当前的缓存容量
     *
     */
    @Override
    public int getTempSize() {
        return tempSize;
    }

    /**
     * 容量满足后的策略
     *
     */
    @Override
    public RefuseStrategies<T> getRefuseStrategies() {
        return refuseStrategies;
    }


    @Contract(pure = true)
    private void moveToSt() {
        for (PlugInUnitDomain pd : container.tVector) {
            refuseStrategies.persist(pd);
        }
        container.tVector.clear();
        currentTmpSize = 0;
    }
}
