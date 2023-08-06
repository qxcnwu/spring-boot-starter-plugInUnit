package com.qxc.threadexector.threadfactorycore;

import com.qxc.threadexector.construct.BasicThreadPool;
import com.qxc.threadexector.ThreadPoolManage;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 16:49
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.threadfactorycore
 */
@Component
@Data
public class ThreadPoolFactoryImpl implements ThreadPoolFactory {
    private final HashMap<String, ExecutorService> javaMap = new HashMap<>();
    private final HashMap<String, ThreadPoolManage> tmMap = new HashMap<>();
    private final HashMap<String, Thread> singleMap = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock rLock = getReadWriteLock().readLock();
    private final Lock wLock = getReadWriteLock().writeLock();

    /**
     * 创建一个单线程的线程池
     */
    @Override
    public ExecutorService getSinglePool(String name) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (javaMap.containsKey(ns)) {
                ns = name + tp++;
            }
            javaMap.put(ns, Executors.newSingleThreadExecutor());
            return javaMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }


    /**
     * 创建多线程的线程池
     */
    @Override
    public ExecutorService getFixPool(String name, int poolSize) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (javaMap.containsKey(ns)) {
                ns = name + tp++;
            }
            javaMap.put(ns, Executors.newFixedThreadPool(poolSize));
            return javaMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 创建ThreadPoolManage
     */
    @Override
    public ThreadPoolManage getThreadPoolMange(String name, int initSize, int maxSize, int coreSize, int queueSize) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (tmMap.containsKey(ns)) {
                ns = name + tp++;
            }
            tmMap.put(ns, new BasicThreadPool(ns, initSize, maxSize, coreSize, queueSize));
            return tmMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 获取单独的一个线程
     */
    @Override
    public Thread getSingleThread(String name, Runnable runnable, boolean setDemon) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (singleMap.containsKey(ns)) {
                ns = name + tp++;
            }
            singleMap.put(ns, new Thread(runnable, ns));
            singleMap.get(ns).setDaemon(setDemon);
            return singleMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 获取当前单独的线程
     */
    @Override
    public Thread getSingleThread(String name, Runnable runnable) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (singleMap.containsKey(ns)) {
                ns = name + tp++;
            }
            singleMap.put(ns, new Thread(runnable, ns));
            return singleMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }

    @Override
    public Thread getSingleThread(ThreadGroup tg, String name, Runnable runnable) {
        wLock.lock();
        try {
            int tp = 1;
            String ns = name;
            while (singleMap.containsKey(ns)) {
                ns = name + tp++;
            }
            singleMap.put(ns, new Thread(tg, runnable, ns));
            return singleMap.get(ns);
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 查询当前全部的线程池
     */
    @Override
    public HashMap<String, ExecutorService> getAllJavaPool() {
        rLock.lock();
        try {
            return javaMap;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 得到所有的基础池
     */
    @Override
    public HashMap<String, ThreadPoolManage> getAllBasicPool() {
        rLock.lock();
        try {
            return tmMap;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取当前全部的单独线程
     */
    @Override
    public HashMap<String, Thread> getAllSingleThread() {
        rLock.lock();
        try {
            return singleMap;
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有JavaPool的名称
     */
    @Override
    public Set<String> getAllJavaPoolName() {
        rLock.lock();
        try {
            return javaMap.keySet();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有线程BasicPool的名称
     */
    @Override
    public Set<String> getAllBasicPoolName() {
        rLock.lock();
        try {
            return tmMap.keySet();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有SingleName的名称
     */
    @Override
    public Set<String> getAllSingleName() {
        rLock.lock();
        try {
            return singleMap.keySet();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有JavaPool的大小
     */
    @Override
    public Integer getAllJavaPoolSize() {
        rLock.lock();
        try {
            return javaMap.keySet().size();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有线程BasicPool的大小
     */
    @Override
    public Integer getAllBasicPoolSize() {
        rLock.lock();
        try {
            return tmMap.keySet().size();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有SingleName的大小
     */
    @Override
    public Integer getAllSingleSize() {
        rLock.lock();
        try {
            return singleMap.keySet().size();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有JavaPool的大小
     */
    @Override
    public Integer getAliveJavaPoolSize() {
        rLock.lock();
        try {
            AtomicInteger i = new AtomicInteger();
            javaMap.values().parallelStream().forEach(a -> {
                if (!a.isShutdown()) {
                    i.getAndIncrement();
                }
            });
            return i.get();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有线程BasicPool的大小
     */
    @Override
    public Integer getAliveBasicPoolSize() {
        rLock.lock();
        try {
            AtomicInteger i = new AtomicInteger();
            tmMap.values().parallelStream().forEach(a -> {
                if (!a.isShutDown()) {
                    i.getAndIncrement();
                }
            });
            return i.get();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有SingleName的大小
     */
    @Override
    public Integer getAliveSingleSize() {
        rLock.lock();
        try {
            AtomicInteger i = new AtomicInteger();
            singleMap.values().parallelStream().forEach(a -> {
                if (!a.isInterrupted()) {
                    i.getAndIncrement();
                }
            });
            return i.get();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有JavaPool的大小
     */
    @Override
    public String[] getAliveJavaPoolString() {
        rLock.lock();
        try {
            ArrayList<String> arr = new ArrayList<>();
            javaMap.keySet().parallelStream().forEach(a -> {
                if (!javaMap.get(a).isShutdown()) {
                    arr.add(a);
                }
            });
            final String[] strings = new String[arr.size()];
            return arr.toArray(strings);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有线程BasicPool的大小
     */
    @Override
    public String[] getAliveBasicPoolString() {
        rLock.lock();
        try {
            ArrayList<String> arr = new ArrayList<>();
            tmMap.keySet().parallelStream().forEach(a -> {
                if (!tmMap.get(a).isShutDown()) {
                    arr.add(a);
                }
            });
            final String[] strings = new String[arr.size()];
            return arr.toArray(strings);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取所有SingleName的大小
     */
    @Override
    public String[] getAliveSingleString() {
        rLock.lock();
        try {
            ArrayList<String> arr = new ArrayList<>();
            singleMap.keySet().parallelStream().forEach(a -> {
                if (!singleMap.get(a).isInterrupted()) {
                    arr.add(a);
                }
            });
            final String[] strings = new String[arr.size()];
            return arr.toArray(strings);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 线程或者线程池是否存活
     */
    @Override
    public Boolean isAlive(String name) {
        return null;
    }

    /**
     * 回收全部的线程池
     */
    @Override
    public Boolean shutdoneByName(String name) {
        wLock.lock();
        try {
            if (javaMap.containsKey(name) && !javaMap.get(name).isShutdown()) {
                javaMap.get(name).shutdown();
                return javaMap.get(name).isShutdown();
            }
            if (tmMap.containsKey(name) && !tmMap.get(name).isShutDown()) {
                tmMap.get(name).shutdown();
                return tmMap.get(name).isShutDown();
            }
            if (singleMap.containsKey(name) && !singleMap.get(name).isInterrupted()) {
                singleMap.get(name).interrupt();
                return singleMap.get(name).isInterrupted();
            }
            return false;
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 关闭全部的线程池
     */
    @Override
    public Boolean shutdoneAll() {
        wLock.lock();
        try {
            javaMap.keySet().forEach(a -> {
                if (!javaMap.get(a).isShutdown()) {
                    javaMap.get(a).shutdown();
                }
            });
            tmMap.keySet().forEach(a -> {
                if (!tmMap.get(a).isShutDown()) {
                    tmMap.get(a).shutdown();
                }
            });
            singleMap.keySet().forEach(a -> {
                if (!singleMap.get(a).isInterrupted()) {
                    singleMap.get(a).interrupt();
                }
            });
            return true;
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 关闭全部的Java线程池
     */
    @Override
    public Boolean shutdoneAllJava() {
        wLock.lock();
        try {
            javaMap.keySet().forEach(a -> {
                if (!javaMap.get(a).isShutdown()) {
                    javaMap.get(a).shutdown();
                }
            });
            return true;
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 关闭全部的手动线程池
     */
    @Override
    public Boolean shutdoneAllTm() {
        wLock.lock();
        try {
            tmMap.keySet().forEach(a -> {
                if (!tmMap.get(a).isShutDown()) {
                    tmMap.get(a).shutdown();
                }
            });
            return true;
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 终止当前全部单独的线程
     */
    @Override
    public Boolean shutdoneAllSingle() {
        singleMap.keySet().forEach(a -> {
            if (!singleMap.get(a).isInterrupted()) {
                singleMap.get(a).interrupt();
            }
        });
        return true;
    }

    /**
     * 清除完成的线程
     *
     * @return
     */
    @Override
    public HashMap<String, Integer> clear() {
        wLock.lock();
        try {
            HashMap<String, Integer> map = new HashMap<>(3);
            int size = 0;
            for (Iterator<Map.Entry<String, ExecutorService>> it = javaMap.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry<String, ExecutorService> item = it.next();
                if (item.getValue().isShutdown()) {
                    it.remove();
                    ++size;
                }
            }
            map.put("javaPool", size);
            // 归0
            size = 0;
            for (Iterator<Map.Entry<String, ThreadPoolManage>> it = tmMap.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry<String, ThreadPoolManage> item = it.next();
                if (item.getValue().isShutDown()) {
                    it.remove();
                    ++size;
                }
            }
            map.put("tmPool", size);
            // 归0
            size = 0;
            for (Iterator<Map.Entry<String, Thread>> it = singleMap.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry<String, Thread> item = it.next();
                if (item.getValue().isInterrupted()) {
                    it.remove();
                    ++size;
                }
            }
            map.put("thread", size);
            return map;
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 线程工厂的单例实现
     */
    @Data
    public static class ThreadFactoryCore {
        private final static ThreadPoolFactory THREAD_POOL_FACTORY = new ThreadPoolFactoryImpl();

        @SuppressWarnings("MissingJavadoc")
        @Contract(pure = true)
        public static ThreadPoolFactory getInstance() {
            return THREAD_POOL_FACTORY;
        }
    }
}
