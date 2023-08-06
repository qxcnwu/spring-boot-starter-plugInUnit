package com.qxc.threadexector.construct;

import com.qxc.threadexector.DenyPolice;
import com.qxc.threadexector.RunnableQueue;
import com.qxc.threadexector.ThreadFactory;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.threadexector.ThreadPoolManage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 邱星晨
 */
@Slf4j
public class BasicThreadPool extends Thread implements ThreadPoolManage {

    /**
     * 初始化线程池大小
     */
    private final int initSize;

    /**
     * 线程池最大大小
     */
    private final int maxSize;

    /**
     * 核心线程大小
     */
    private final int coreSize;

    /**
     * 当前活跃线程大小
     */
    private int activeSize;

    /**
     * 线程工厂
     */
    private static ThreadFactory threadFactory;

    /**
     * 任务队列
     */
    private final RunnableQueue runnableQueue;

    /**
     * 线程池是否被shutdown
     */
    private volatile boolean shutdown = false;

    /**
     * 工作线程队列
     */
    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();

    /**
     * 默认线程满策略
     */
    private final static DenyPolice DENY_POLICE = new DenyPolice.RunnerDenyPolice();

    /**
     * 默认线程工厂
     */
    private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    /**
     * 设置超时
     */
    private final TimeUnit timeUnit;

    /**
     * 更新时间
     */
    private final long keepAlive;


    @SuppressWarnings("MissingJavadoc")
    public BasicThreadPool(String name, int initSize, int maxSize, int coreSize, int queueSize, ThreadFactory threadFactorys, DenyPolice denyPolice, TimeUnit timeUnit, long keepAlive) {
        /*
          线程池名称
         */
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        threadFactory = threadFactorys;
        this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolice, this);
        this.timeUnit = timeUnit;
        this.keepAlive = keepAlive;
        this.init();
    }

    @SuppressWarnings("MissingJavadoc")
    public BasicThreadPool(String name, int initSize, int maxSize, int coreSize, int queueSize) {
        this(name, initSize, maxSize, coreSize, queueSize, DEFAULT_THREAD_FACTORY, DENY_POLICE, TimeUnit.SECONDS, 5);
    }

    /**
     * 创建初始化
     */
    private void init() {
        start();
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    /**
     * 为线程池中创建新线程
     */
    private void newThread() {
        //创建线程启动任务
        InternalTask internalTask = new InternalTask(runnableQueue);
        Thread thread = threadFactory.createThread(internalTask);
        ThreadTask threadTask = new ThreadTask(thread, internalTask);
        threadQueue.offer(threadTask);
        this.activeSize++;
        thread.start();
    }

    /**
     * 关闭一个线程
     *
     */
    private void removeThread() {
        ThreadTask threadTask = threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeSize--;
    }

    /**
     * 维护大小
     */
    @Override
    public void run() {
        while (!shutdown && !isInterrupted()) {
            try {
                timeUnit.sleep(keepAlive);
            } catch (InterruptedException e) {
                shutdown = true;
                break;
            }
            synchronized (this) {
                if (shutdown) {
                    break;
                }
                //当前队列尚有任务没有处理，并且 则继续扩容
                if (runnableQueue.size() > 0 && activeSize < coreSize) {
                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    log.info("1 runnableQueue size expand to " + coreSize);
                    //不让线程扩容直接达到maxsize
                    continue;
                }
                //如果队列中有任务没有处理，并且 则继续扩容到maxsize
                if (runnableQueue.size() > 0 && activeSize < maxSize) {
                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    log.info("2 runnableQueue size expand to " + maxSize);
                }
                //如果队列中没有任务则回收到coreSize
                if (runnableQueue.size() == 0 && activeSize > coreSize) {
                    for (int i = coreSize; i < activeSize; i++) {
                        removeThread();
                    }
                }
            }
        }
    }


    /**
     * 按照名称删除
     *
     */
    @Override
    public void removeThread(String name) {
        if (!runnableQueue.remove(name)) {
            for (ThreadTask tk : threadQueue) {
                if (tk.getNameAndStop(name)) {
                    tk.start();
                    log.info(getName() + " server stopped:" + name);
                    break;
                }
            }
        }
    }


    /**
     * 执行线程，返回对应的线程名称
     *
     */
    @Override
    public void execute(RunableWithName runnable) {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        this.runnableQueue.offer(runnable);
    }

    /**
     * 添加线程
     *
     */
    @Override
    public String execute(@NotNull RunableWithName runnable, String name) {
        runnable.setName(name);
        execute(runnable);
        return name;
    }

    /**
     * 查询对应线程是不是关闭
     *
     */
    @Override
    public boolean isInterrupt(String name) {
        return false;
    }


    /**
     * 查询是否存在name的线程
     *
     */
    @Override
    public boolean contains(String name) {
        if (runnableQueue.contains(name)) {
            return true;
        }
        for (ThreadTask tk : threadQueue) {
            if (tk.nameEqual(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭线程池
     */
    @Override
    public void shutdown() {
        synchronized (this) {
            if (shutdown) {
                return;
            }
            shutdown = true;
            threadQueue.forEach(threadTask -> {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });
        }
    }

    /**
     * 获取线程池初始大小
     *
     */
    @Override
    public int getInitSize() {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        return initSize;
    }

    /**
     * 获取线程池的最大数量
     *
     */
    @Override
    public int getMaxSize() {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        return maxSize;
    }

    /**
     * 获取当前线程池核心线程数量大小
     *
     */
    @Override
    public int getCoreSize() {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        return coreSize;
    }

    /**
     * 获取线程池中用于缓存任务队列的大小
     *
     */
    @Override
    public int getQueueSize() {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        return runnableQueue.size();
    }

    /**
     * 获取线程池中活动线程的数量
     *
     */
    @Override
    public int getActiveCount() {
        if (this.shutdown) {
            throw new IllegalStateException("This thread pool is shutdown");
        }
        return activeSize;
    }

    /**
     * 线程池是否关闭
     *
     */
    @Override
    public boolean isShutDown() {
        return shutdown;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
        private static final ThreadGroup GROUP = new ThreadGroup("ThreadPool-" + GROUP_COUNTER.getAndIncrement());
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Contract("_ -> new")
        @Override
        public @NotNull Thread createThread(Runnable runnable) {
            return ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSingleThread(GROUP, "DefaultThreadFactory-" + COUNTER.getAndIncrement(), runnable);
        }
    }

    private static class ThreadTask {
        @Contract(pure = true)
        public ThreadTask(Thread thread, InternalTask internalTask) {
            this.thread = thread;
            this.internalTask = internalTask;
        }

        final Thread thread;
        final InternalTask internalTask;

        /**
         * 返回当前线程执行的线程的名称
         *
         */
        boolean getNameAndStop(String name) {
            return internalTask.getNameAndStop(name);
        }

        boolean nameEqual(String name) {
            return internalTask.nameEqual(name);
        }

        public void start() {
            internalTask.start();
        }
    }
}