package com.qxc.threadexector;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:14
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector
 */
@FunctionalInterface
public interface ThreadFactory {
    /**
     * 创建线程
     */
    Thread createThread(Runnable runnable);
}
