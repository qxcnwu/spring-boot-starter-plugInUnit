package com.qxc.threadexector.eventbus;

import java.util.concurrent.Executor;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 10:04
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
public class EventBus implements Bus{
    private final Registry registry=new Registry();
    private final String busName;
    private final static String DEFAULT_BUS_NAME = "default";
    private final static String DEFAULT_TOPIC = "default-topic";
    /**
     * 分发广播消息的类
     */
    private final Dispatcher dispatcher;

    @SuppressWarnings("MissingJavadoc")
    public EventBus(){
        this(DEFAULT_BUS_NAME,null,Dispatcher.SEQ_EXECTOR_SERVICE);
    }

    @SuppressWarnings("MissingJavadoc")
    public EventBus(String busName){
        this(busName,null,Dispatcher.SEQ_EXECTOR_SERVICE);
    }

    @SuppressWarnings("MissingJavadoc")
    public EventBus(Executor executor){
        this(DEFAULT_BUS_NAME,null,executor);
    }

    @SuppressWarnings("MissingJavadoc")
    public EventBus(String busName, EventExceptionHandler exceptionHandler, Executor executor){
        this.busName=busName;
        this.dispatcher=Dispatcher.newDispatcher(exceptionHandler,executor);
    }

    @SuppressWarnings("MissingJavadoc")
    public EventBus(EventExceptionHandler exceptionHandler){
        this(DEFAULT_BUS_NAME,exceptionHandler,Dispatcher.SEQ_EXECTOR_SERVICE);
    }


    /**
     * 将注册动作直接委托给Registry
     */
    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }

    /**
     * 结束注册同样交给委托Registry
     */
    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }

    /**
     * 默认提交
     */
    @Override
    public void post(Object event) {
        this.post(event,DEFAULT_TOPIC);
    }

    /**
     * 通过dispatcher提交到事件
     */
    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this,registry,event,topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}
