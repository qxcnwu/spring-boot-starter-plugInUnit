package com.qxc.threadexector.eventbus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 10:05
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
class Registry {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>> subList = new ConcurrentHashMap<>();

    /**
     * 设置绑定
     *
     */
    public void bind(Object subscriber) {
        List<Method> subMethods = getSubMethod(subscriber);
        subMethods.forEach(m -> tierSubscriber(subscriber, m));
    }

    /**
     * 取消绑定
     *
     */
    public void unbind(Object subscriber) {
        subList.forEach((key, queue) -> queue.forEach(s -> {
            if (s.getSubscribObject().equals(subscriber)) {
                s.setDisable(true);
            }
        }));
    }

    /**
     * 扫描所有注册在topic上的对象
     *
     */
    public ConcurrentLinkedQueue<Subscriber> scanSubscriber(String topic) {
        return subList.get(topic);
    }


    /**
     *
     */
    private void tierSubscriber(Object subscriber, @NotNull Method m) {
        final Subscribe subscribe = m.getDeclaredAnnotation(Subscribe.class);
        String[] topics = subscribe.topic();
        for (String topic : topics) {
            subList.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
            subList.get(topic).add(new Subscriber(subscriber, m));
        }
    }


    /**
     * 得到类上所有含Subscriber的方法
     */
    private @NotNull List<Method> getSubMethod(@NotNull Object subscriber) {
        final List<Method> methods = new ArrayList<>();
        Class<?> temp = subscriber.getClass();
        while (temp != null) {
            Method[] deM = temp.getDeclaredMethods();
            Arrays.stream(deM).filter(m -> m.isAnnotationPresent(Subscribe.class) &&
                    m.getParameterCount() == 1 &&
                    m.getModifiers() == Modifier.PUBLIC).forEach(methods::add);
            temp = temp.getSuperclass();
        }
        return methods;
    }
}
