package com.qxc.threadexector;


import com.qxc.threadexector.construct.RunableWithName;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:13
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector
 */
public interface RunnableQueue {
    /**
     * 添加新任务
     *
     */
    void offer(RunableWithName runnable);

    /**
     * 工作线程通过take方法获取Runnable
     *
     */
    RunableWithName take() throws InterruptedException;

    /**
     * 获取任务队列中任务数量
     *
     */
    int size();

    /**
     * 删除name对应的文件
     *
     */
    boolean remove(String name);

    /**
     * 查询queue中是否含有name名称的线程
     */
    boolean contains(String name);
}
