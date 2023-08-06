package com.qxc.threadexector.eventbus;

import org.jetbrains.annotations.NotNull;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 10:07
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
public interface EventExceptionHandler {
    /**
     * 抓取错误
     *
     */
    default void handle(@NotNull Throwable th, EventContext eventContext) {
        System.out.println(th.getMessage() + ":" + eventContext);
    }
}
