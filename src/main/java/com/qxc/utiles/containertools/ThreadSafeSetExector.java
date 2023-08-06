package com.qxc.utiles.containertools;

import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.reflectancetools.CloneInstance;
import com.qxc.utiles.reflectancetools.PipeLine;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author qxc
 * @Date 2023 2023/7/11 19:12
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @param <E>
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Accessors(chain = true)
public class ThreadSafeSetExector<T, E extends CloneInstance<E>> extends Thread {
    private final static Object MUTEX = new Object();
    private final ConSafeQueue<T> conSafeQueue;
    private final TimeUnit timeUnit;
    private final long updateLittleTime;
    private final int updateLimitNumber;
    private boolean isShutdown = false;

    private final PipeLine<E> pipeLine;

    private Thread listenThread;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final int UPDATE_LITTLE_TIME = 10;
    private static final int UPDATE_LIMIT_NUMBER = 10;


    @SuppressWarnings("MissingJavadoc")
    public ThreadSafeSetExector(PipeLine<E> pipeLine) {
        this(TIME_UNIT, UPDATE_LITTLE_TIME, UPDATE_LIMIT_NUMBER, pipeLine);
    }

    @SuppressWarnings("MissingJavadoc")
    public ThreadSafeSetExector(int limitTime, int limitNumber, PipeLine<E> pipeLine) {
        this(TimeUnit.SECONDS, limitTime, limitNumber, pipeLine);
    }

    @SuppressWarnings("MissingJavadoc")
    public ThreadSafeSetExector(@NotNull TimeUnit timeUnit, int updateLittleTime, int updateLimitNumber, PipeLine<E> pipeLine) {
        this.timeUnit = timeUnit;
        this.pipeLine = pipeLine;
        this.updateLittleTime = (long) (updateLittleTime
                * (Math.pow(1000, Math.min(1, timeUnit.ordinal() - 2)))
                * Math.pow(60, Math.max(0, timeUnit.ordinal() - 3))
                * Math.pow(24, Math.max(0, timeUnit.ordinal() - 6)));
        this.updateLimitNumber = updateLimitNumber;
        conSafeQueue = new ConSafeQueue<>(this.updateLittleTime, this.updateLimitNumber);
        listenThread = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSingleThread("ThreadSafeSetExector-Listen", this::listen);
        log.debug("init updateLittleTime:{} ms,updateLimitNumber:{}", this.updateLittleTime, this.updateLimitNumber);
    }


    /**
     * 执行条件是到达写入上限，或者现在是常规写入状态
     */
    @Override
    public void run() {
        // 执行监听线程
        listenThread.start();
        while (!isShutdown && !isInterrupted()) {
            // 循环查询始终刷新
            synchronized (this) {
                while (!conSafeQueue.shouldUpdate()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        log.info(e.getMessage() + " error in ThreadSafeSetExector run function");
                    }
                }
                final ArrayList<T> allT = conSafeQueue.getAll();
                try {
                    log.info(allT + " has been get from document update queue!!");
                    pipeLine.exec(allT);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.notifyAll();
            }
        }
    }

    /**
     * 设置多线程添加
     *
     */
    public synchronized void add(T obj) {
        if (!isShutdown && !isInterrupted()) {
            while (conSafeQueue.shouldUpdate()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    log.info(e.getMessage() + " error in ThreadSafeSetExector run function");
                }
            }
            log.info(obj + " add to update document queue " + conSafeQueue.shouldUpdate());
            conSafeQueue.put(obj);
            this.notifyAll();
        } else {
            throw new RuntimeException("ThreadSafeSetExector has been shutdone");
        }
    }

    /**
     * 立即刷新
     */
    public synchronized void refresh() {
        if (!isShutdown && !isInterrupted()) {
            final ArrayList<T> allT = conSafeQueue.getAll();
            log.debug("recover:" + allT);
            try {
                pipeLine.exec(allT);
            } catch (ExecutionException | InterruptedException e) {
                log.error(e.getMessage());
            }
            this.notifyAll();
        }
    }

    /**
     * 监听
     */
    public void listen() {
        while (!isShutdown && !isInterrupted()) {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    /**
     * 移除对象
     */
    public void remove(T obj) {
        synchronized (this) {
            if (!isShutdown && !isInterrupted()) {
                conSafeQueue.remove(obj);
                this.notifyAll();
            } else {
                throw new RuntimeException("ThreadSafeSetExector has been shutdone");
            }
        }
    }

    /**
     * 清除所有线程
     */
    public synchronized void clear() {
        if (!isShutdown && !isInterrupted()) {
            conSafeQueue.clear();
            this.notifyAll();
        } else {
            throw new RuntimeException("ThreadSafeSetExector has been shutdone");
        }
    }

    /**
     * 终止线程
     */
    public void shutdone() {
        isShutdown = true;
    }

    /**
     * 创建新的线程安全容器
     *
     * @param <T>
     */
    public static class ConSafeQueue<T> {
        private final Queue<T> conSet = new LinkedList<>();
        private long currentTime;
        private final long limitTime;
        private final int limitNumber;

        /**
         * 读写锁
         */
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock wLock = lock.writeLock();
        private final Lock rLock = lock.readLock();

        @SuppressWarnings("MissingJavadoc")
        public ConSafeQueue(long limitTime, int limitNumber) {
            currentTime = System.currentTimeMillis();
            this.limitTime = limitTime;
            this.limitNumber = limitNumber;
            log.info(limitTime + "s and " + limitNumber + " to create document update queue!!");
        }

        /**
         * 获取当前时间
         *
         */
        public long getCurrentTime() {
            rLock.lock();
            try {
                return currentTime;
            } finally {
                rLock.unlock();
            }
        }

        /**
         * 获取当前的容器容量
         *
         */
        public int size() {
            rLock.lock();
            try {
                return conSet.size();
            } finally {
                rLock.unlock();
            }
        }

        /**
         * 置入对象
         *
         */
        public void put(T obj) {
            wLock.lock();
            try {
                if (conSet.contains(obj)) {
                    return;
                }
                if (conSet.isEmpty()) {
                    currentTime = System.currentTimeMillis();
                }
                conSet.add(obj);
            } finally {
                wLock.unlock();
            }
        }

        /**
         * 移除对应的对象
         *
         */
        public void remove(T obj) {
            wLock.lock();
            try {
                conSet.remove(obj);
            } finally {
                wLock.unlock();
            }
        }

        /**
         * 清空对象
         */
        public void clear() {
            wLock.lock();
            try {
                conSet.clear();
                currentTime = System.currentTimeMillis();
            } finally {
                wLock.unlock();
            }
        }

        /**
         * 查看是否是空
         *
         */
        public boolean isEmpty() {
            rLock.lock();
            try {
                return conSet.isEmpty();
            } finally {
                rLock.unlock();
            }
        }

        /**
         * 返回所有元素
         *
         */
        public ArrayList<T> getAll() {
            wLock.lock();
            try {
                ArrayList<T> arr = new ArrayList<>();
                while (!conSet.isEmpty()) {
                    arr.add(conSet.poll());
                }
                currentTime = System.currentTimeMillis();
                return arr;
            } finally {
                wLock.unlock();
            }
        }

        @SuppressWarnings("MissingJavadoc")
        public boolean shouldUpdate() {
            rLock.lock();
            try {
                return conSet.size() >= limitNumber || System.currentTimeMillis() - currentTime > limitTime;
            } finally {
                rLock.unlock();
            }
        }
    }
}
