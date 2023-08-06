package com.qxc.threadexector.eventbus;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 9:51
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
public interface Bus {
    /**
     * 将某个对象注册到BUS上使其成为Subscriber
     *
     */
    void register(Object subscriber);

    /**
     * 取消注册
     *
     */
    void unregister(Object subscriber);

    /**
     * 提交event到默认topic
     *
     */
    void post(Object event);

    /**
     * 提交event到指定的topic
     */
    void post(Object event, String topic);

    /**
     * 关闭Bus
     */
    void close();

    /**
     * 返回Bus的名称
     */
    String getBusName();
}
