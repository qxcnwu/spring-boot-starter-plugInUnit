package com.qxc.threadexector.construct;

import com.qxc.threadexector.DenyPolice;
import com.qxc.threadexector.RunnableQueue;
import com.qxc.threadexector.ThreadPoolManage;

import java.util.LinkedList;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:28
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.construct
 */
public class LinkedRunnableQueue implements RunnableQueue {
    /**
     * 最大限度
     */
    private final int limit;

    /**
     * 若队列中的线程满了则需要执行拒绝策略
     */
    private final DenyPolice denyPolice;

    /**
     * 存放任务的队列
     */
    private final LinkedList<RunableWithName> runnables = new LinkedList<>();

    private final ThreadPoolManage threadPool;

    @SuppressWarnings("MissingJavadoc")
    public LinkedRunnableQueue(int limit, DenyPolice denyPolice, ThreadPoolManage threadPool) {
        this.limit = limit;
        this.denyPolice = denyPolice;
        this.threadPool = threadPool;
    }

    /**
     * 添加新任务
     *
     */
    @Override
    public void offer(RunableWithName runnable) {
        synchronized (runnables) {
            if (runnables.size() >= limit) {
                //执行拒绝策略
                denyPolice.rejectedExecution(runnable.getRun(), threadPool);
            } else {
                //可以执行放入队列，唤醒所有等待线程
                runnables.addLast(runnable);
                runnables.notifyAll();
            }
        }
    }

    /**
     * 工作线程通过take方法获取Runnable
     *
     */
    @Override
    public RunableWithName take() throws InterruptedException {
        synchronized (runnables) {
            while (runnables.isEmpty()) {
                //如果其中没有线程进入等待
                runnables.wait();
            }
            return runnables.removeFirst();
        }
    }

    /**
     * 获取任务队列中任务数量
     *
     */
    @Override
    public int size() {
        synchronized (runnables) {
            return runnables.size();
        }
    }

    /**
     * 删除name对应的文件
     *
     */
    @Override
    public boolean remove(String name) {
        synchronized (runnables) {
            for (RunableWithName run : runnables) {
                if(run.getName().equals(name)){
                    return runnables.remove(run);
                }
            }
            return false;
        }
    }

    /**
     * 查询queue中是否含有name名称的线程
     *
     */
    @Override
    public boolean contains(String name) {
        synchronized (runnables) {
            for (RunableWithName run : runnables) {
                if(run.getName().equals(name)){
                    return true;
                }
            }
            return false;
        }
    }
}
