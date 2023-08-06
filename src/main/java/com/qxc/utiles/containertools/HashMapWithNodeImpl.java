package com.qxc.utiles.containertools;

import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 23:03
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.containertools
 */
@Data
@NoArgsConstructor
@Slf4j
public class HashMapWithNodeImpl implements HashMapWithNode {
    private final HashMap<String, ArrayList<PlugInUnitContextWithNode>> map = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock wlock = readWriteLock.writeLock();
    private final Lock rlock = readWriteLock.readLock();

    /**
     * 插入plug
     *
     * @param plug
     */
    @Override
    public void insert(@NotNull NewPlugInUnitWithContext plug) {
        wlock.lock();
        try {
            // 首先查询是否具有这个对象
            final String name = plug.getPlugInUnitName();
            // 首先刷新全部置为0
            setAllLatestNodeZero(name);
            PlugInUnitContextWithNode pn = new PlugInUnitContextWithNode().setContext(plug).setLatest(true);
            if (!map.containsKey(name)) {
                ArrayList<PlugInUnitContextWithNode> tp = new ArrayList<>();
                map.put(name, tp);
            }
            // 插入对应的位置
            map.get(name).add(pn);
        } finally {
            wlock.unlock();
        }
    }

    @Override
    public void insert(@NotNull NewPlugInUnitWithContext plug, boolean isLatest) {
        wlock.lock();
        try {
            // 首先查询是否具有这个对象
            final String name = plug.getPlugInUnitName();
            // 首先刷新全部置为0
            if (map.containsKey(name)) {
                map.get(name).forEach(pn -> {
                    pn.context.close();
                    pn.setLatest(false);
                });
            }
            PlugInUnitContextWithNode pn = new PlugInUnitContextWithNode().setContext(plug).setLatest(isLatest);
            if (!map.containsKey(name)) {
                ArrayList<PlugInUnitContextWithNode> tp = new ArrayList<>();
                map.put(name, tp);
            }
            // 插入对应的位置
            map.get(name).add(pn);
        } finally {
            wlock.unlock();
        }
    }

    private void setAllLatestNodeZero(String name) {
        if (map.containsKey(name)) {
            map.get(name).forEach(pn -> {
                pn.context.close();
                pn.setLatest(false);
            });
        }
    }

    /**
     * 删除对应的对象
     *
     * @param pName
     * @param version
     */
    @Override
    public void delete(String pName, String version) {
        wlock.lock();
        try {
            // 首先查询是否具有这个对象
            if (!map.containsKey(pName)) {
                log.error("HashMapWithNodeImpl no such object with name:{}", pName);
                return;
            }
            // 指针指向当前的删除的元素是否是运行中的元素
            final boolean[] deleteRunning = {false};
            // 查询是否有此版本并且进行删除
            map.get(pName).removeIf(new Predicate<>() {
                /**
                 * Evaluates this predicate on the given argument.
                 *
                 * @param plugInUnitContextWithNode the input argument
                 * @return {@code true} if the input argument matches the predicate,
                 * otherwise {@code false}
                 */
                @Override
                public boolean test(PlugInUnitContextWithNode plugInUnitContextWithNode) {
                    if (plugInUnitContextWithNode.context.getVersionName().equals(version)) {
                        // 关闭当前服务
                        plugInUnitContextWithNode.context.close();
                        deleteRunning[0] = plugInUnitContextWithNode.isLatest();
                        return true;
                    }
                    return false;
                }
            });
            // 后处理需要重新重启动一个服务
            if (deleteRunning[0]) {
                if (map.get(pName).isEmpty()) {
                    return;
                }
                map.get(pName).get(map.get(pName).size() - 1).setLatest(true);
                map.get(pName).get(map.get(pName).size() - 1).getContext().rStart();
            }
        } finally {
            wlock.unlock();
        }
    }


    /**
     * 删除对象
     *
     * @param plug
     */
    @Override
    public void delete(@NotNull NewPlugInUnitWithContext plug) {
        delete(plug.getPlugInUnitName(), plug.getVersionName());
    }

    /**
     * 查询对应的对象是否存在
     *
     * @param plug
     */
    @Override
    public boolean contains(@NotNull NewPlugInUnitWithContext plug) {
        rlock.lock();
        try {
            final String name = plug.getPlugInUnitName();
            // 不存在当前的插件名称
            if (!map.containsKey(name)) {
                return false;
            }
            final String version = plug.getVersionName();
            // 判断是否有当前版本的插件
            return map.get(name).stream().anyMatch(plugInUnitContextWithNode -> plugInUnitContextWithNode.getContext().getVersionName().equals(version));
        } finally {
            rlock.unlock();
        }
    }

    /**
     * 查询
     *
     * @param pName
     * @param version
     */
    @Override
    public boolean contains(String pName, String version) {
        rlock.lock();
        try {
            // 不存在当前的插件名称
            if (!map.containsKey(pName)) {
                return false;
            }
            // 判断是否有当前版本的插件
            return map.get(pName).stream().anyMatch(plugInUnitContextWithNode -> plugInUnitContextWithNode.getContext().getVersionName().equals(version));
        } finally {
            rlock.unlock();
        }
    }


    /**
     * 是否是最新的插件
     *
     * @param plug
     * @return
     */
    @Override
    public boolean isLatest(@NotNull NewPlugInUnitWithContext plug) {
        rlock.lock();
        try {
            final PlugInUnitContextWithNode byName = getByName(plug.getPlugInUnitName(), plug.getVersionName());
            if (byName != null) {
                return byName.isLatest();
            }
            return false;
        } finally {
            rlock.unlock();
        }
    }

    /**
     * 是否是最新的插件
     *
     * @param pName
     * @param version
     * @return
     */
    @Override
    public boolean isLatest(String pName, String version) {
        rlock.lock();
        try {
            final PlugInUnitContextWithNode byName = getByName(pName, version);
            if (byName != null) {
                return byName.isLatest();
            }
            return false;
        } finally {
            rlock.unlock();
        }
    }

    /**
     * 将这个值设置为当前版本
     *
     * @param pName
     * @param version
     * @return
     */
    @Override
    public boolean setLatest(String pName, String version) {
        wlock.lock();
        try {
            // 如果没有这个对象那么就返回false
            if (!map.containsKey(pName)) {
                log.error("HashMapWithNodeImpl no such object with name:{}", pName);
                return false;
            }
            // 将所有版本停止服务
            setAllLatestNodeZero(pName);
            // 启动当前版本
            for (PlugInUnitContextWithNode pn : map.get(pName)) {
                if (pn.context.getVersionName().equals(version)) {
                    // 查询到当前版本那么开启对应的服务
                    pn.setLatest(true);
                    pn.context.rStart();
                    return true;
                }
            }
            return false;
        } finally {
            wlock.unlock();
        }
    }

    /**
     * 获取当前执行插件
     *
     * @param name
     * @return
     */
    @Override
    public NewPlugInUnitWithContext getLatest(String name) {
        rlock.lock();
        try {
            if (!map.containsKey(name)) {
                return null;
            }
            final Optional<PlugInUnitContextWithNode> node = map.get(name).stream().filter(PlugInUnitContextWithNode::isLatest).findFirst();
            NewPlugInUnitWithContext context = node.map(PlugInUnitContextWithNode::getContext).orElse(null);
            // 证明当前没有开启的服务返回最后一个
            if (context == null) {
                context = map.get(name).get(map.get(name).size() - 1).getContext();
            }
            return context;
        } finally {
            rlock.unlock();
        }
    }

    /**
     * 清除容器内部所有的对象
     */
    @Override
    public void clear() {
        wlock.lock();
        try {
            // 首先关闭所有运行的对象
            for (String key : map.keySet()) {
                for (PlugInUnitContextWithNode pn : map.get(key)) {
                    if (pn.isLatest()) {
                        pn.getContext().close();
                    }
                }
            }
            map.clear();
        } finally {
            wlock.unlock();
        }
    }

    /**
     * 通过插件名称以及版本获取对象
     * 当前方法调用的时候不上锁，用户端进行上锁
     * 此函数因该用于private调用
     *
     * @param name
     * @param version
     * @return
     */
    @Override
    public PlugInUnitContextWithNode getByName(String name, String version) {
        if (map.containsKey(name)) {
            for (PlugInUnitContextWithNode context : map.get(name)) {
                if (context.getContext().getVersionName().equals(version)) {
                    return context;
                }
            }
        }
        return null;
    }

    public List<PlugInUnitContextWithNode> getAll(boolean latest) {
        ArrayList<PlugInUnitContextWithNode> arr = new ArrayList<>();
        for (String key : map.keySet()) {
            if (latest) {
                for (PlugInUnitContextWithNode pn : map.get(key)) {
                    if (pn.isLatest()) {
                        arr.add(pn);
                        break;
                    }
                }
            } else {
                arr.addAll(map.get(key));
            }
        }
        return arr;
    }

    public List<PlugInUnitContextWithNode> getAll(String pName) {
        return map.get(pName);
    }

    @Data
    @Accessors(chain = true)
    public static class PlugInUnitContextWithNode {
        private boolean isLatest;
        private NewPlugInUnitWithContext context;
    }
}
