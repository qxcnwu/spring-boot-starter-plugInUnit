package com.qxc.threadexector.threadfactorycore;

import com.qxc.threadexector.ThreadPoolManage;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 16:33
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.threadfactorycore
 */
public interface ThreadPoolFactory {
    /**
     * 创建一个单线程的线程池
     */
    ExecutorService getSinglePool(String name);

    /**
     * 创建多线程的线程池
     */
    ExecutorService getFixPool(String name, int poolSize);

    /**
     * 创建ThreadPoolManage
     */
    ThreadPoolManage getThreadPoolMange(String name, int initSize, int maxSize, int coreSize, int queueSize);

    /**
     * 获取单独的一个线程
     */
    Thread getSingleThread(String name, Runnable runnable, boolean setDemon);

    /**
     * 获取当前单独的线程
     */
    Thread getSingleThread(String name, Runnable runnable);


    /**
     * 将线程加入线程组
     */
    Thread getSingleThread(ThreadGroup tg, String name, Runnable runnable);

    /**
     * 查询当前全部的线程池
     */
    HashMap<String, ExecutorService> getAllJavaPool();

    /**
     * 得到所有的基础池
     */
    HashMap<String, ThreadPoolManage> getAllBasicPool();

    /**
     * 获取当前全部的单独线程
     */
    HashMap<String, Thread> getAllSingleThread();

    /**
     * 获取所有JavaPool的名称
     */
    Set<String> getAllJavaPoolName();

    /**
     * 获取所有线程BasicPool的名称
     */
    Set<String> getAllBasicPoolName();

    /**
     * 获取所有SingleName的名称
     */
    Set<String> getAllSingleName();

    /**
     * 获取所有JavaPool的大小
     */
    Integer getAllJavaPoolSize();

    /**
     * 获取所有线程BasicPool的大小
     */
    Integer getAllBasicPoolSize();

    /**
     * 获取所有SingleName的大小
     */
    Integer getAllSingleSize();

    /**
     * 获取所有JavaPool的大小
     */
    Integer getAliveJavaPoolSize();

    /**
     * 获取所有线程BasicPool的大小
     */
    Integer getAliveBasicPoolSize();

    /**
     * 获取所有SingleName的大小
     */
    Integer getAliveSingleSize();

    /**
     * 获取所有JavaPool的大小
     */
    String[] getAliveJavaPoolString();

    /**
     * 获取所有线程BasicPool的大小
     */
    String[] getAliveBasicPoolString();

    /**
     * 获取所有SingleName的大小
     */
    String[] getAliveSingleString();


    /**
     * 线程或者线程池是否存活
     */
    Boolean isAlive(String name);


    /**
     * 回收全部的线程池
     */
    Boolean shutdoneByName(String name);

    /**
     * 关闭全部的线程池
     */
    Boolean shutdoneAll();

    /**
     * 关闭所有java的基础线程池
     */
    Boolean shutdoneAllJava();

    /**
     * 关闭所有的自定义线程池
     */
    Boolean shutdoneAllTm();

    /**
     * 终止当前全部单独的线程
     */
    Boolean shutdoneAllSingle();

    HashMap<String, Integer> clear();
}
