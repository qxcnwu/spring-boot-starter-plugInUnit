package com.qxc.threadexector.eventbus;

import java.util.concurrent.Executor;

/**
 * 异步EventBus
 * @author 邱星晨
 */
public class AsyncEventBus extends EventBus{
    AsyncEventBus(String busName, EventExceptionHandler eventExceptionHandler, Executor executor){
        super(busName,eventExceptionHandler,executor);
    }

    @SuppressWarnings("MissingJavadoc")
    public AsyncEventBus(String busName, Executor threadPoolExecutor){
        this(busName,null,threadPoolExecutor);
    }

    @SuppressWarnings("MissingJavadoc")
    public AsyncEventBus(Executor threadPoolExecutor){
        this("default-async",threadPoolExecutor);
    }

    AsyncEventBus(EventExceptionHandler eventExceptionHandler, Executor executor){
        this("default-async",eventExceptionHandler,executor);
    }
}
