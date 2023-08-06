package com.qxc.threadexector;

import com.qxc.threadexector.construct.RunableWithName;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 14:55
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector
 */
public interface ThreadPoolManage {
    /**
     * 获取当前线程池的名称
     *
     */
    String getName();

    /**
     * 执行线程，返回对应的线程名称
     *
     */
    void execute(RunableWithName runnable);

    /**
     * 添加线程
     *
     */
    String execute(RunableWithName runnable, String name);

    /**
     * 停止对应的线程
     *
     */
    void removeThread(String name);

    /**
     * 查询对应线程是不是关闭
     *
     */
    boolean isInterrupt(String name);


    /**
     * 查询是否存在name的线程
     *
     */
    boolean contains(String name);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 获取线程池初始大小
     *
     */
    int getInitSize();

    /**
     * 获取线程池的最大数量
     *
     */
    int getMaxSize();

    /**
     * 获取当前线程池核心线程数量大小
     *
     */
    int getCoreSize();

    /**
     * 获取线程池中用于缓存任务队列的大小
     *
     */
    int getQueueSize();

    /**
     * 获取线程池中活动线程的数量
     *
     */
    int getActiveCount();

    /**
     * 线程池是否关闭
     *
     */
    boolean isShutDown();
}
