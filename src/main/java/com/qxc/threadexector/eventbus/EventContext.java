package com.qxc.threadexector.eventbus;

import java.lang.reflect.Method;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 10:46
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
public interface EventContext {
    /**
     * 获取资源
     * @return String
     */
    String getRource();

    /**
     * 获取Subscriber
     * @return Object
     */
    Object getSubscriber();

    /**
     * 获取Subscribe
     * @return Method
     */
    Method getSubscribe();

    /**
     * 获取EVENT事件
     * @return EVENT Object
     */
    Object getEvent();
}
