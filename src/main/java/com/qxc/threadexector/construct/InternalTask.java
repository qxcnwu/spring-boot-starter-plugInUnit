package com.qxc.threadexector.construct;

import com.qxc.threadexector.RunnableQueue;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:25
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.construct
 */
@Slf4j
public class InternalTask implements Runnable {
    private final RunnableQueue runnableQueue;
    private final static Object MUTEX = new Object();
    private final static String DEFAULT = "default";
    private volatile boolean running = true;

    @SuppressWarnings("MissingJavadoc")
    public String name = DEFAULT;
    @SuppressWarnings("MissingJavadoc")
    public boolean currentDone = false;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public InternalTask(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            RunableWithName task;
            try {
                task = runnableQueue.take();
                synchronized (MUTEX) {
                    name = task.getName();
                    currentDone = false;
                }
                log.info("Task " + name + " start running!!");
                task.getRun().run();
                log.info("Task " + name + " done!!");
                synchronized (MUTEX) {
                    currentDone = true;
                    name = DEFAULT;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("MissingJavadoc")
    public void stop() {
        this.running = false;
    }

    @SuppressWarnings("MissingJavadoc")
    public boolean getNameAndStop(@NotNull String name) {
        synchronized (MUTEX) {
            if (name.equals(this.name) && !currentDone) {
                stop();
                return true;
            }
            return false;
        }
    }

    @SuppressWarnings("MissingJavadoc")
    public boolean nameEqual(@NotNull String name) {
        synchronized (MUTEX) {
            return name.equals(this.name);
        }
    }

    @SuppressWarnings("MissingJavadoc")
    public void start() {
        this.running = false;
        name = DEFAULT;
        currentDone = false;
    }
}
