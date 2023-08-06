package com.qxc.threadexector.eventbus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Method;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 10:08
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
@Data
@Slf4j
@AllArgsConstructor
public class Subscriber {
    private final Object subscribeObject;
    private final Method subscribeMethod;
    private boolean disable = false;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public Subscriber(Object subscribe, Method m) {
        subscribeMethod = m;
        subscribeObject = subscribe;
    }

    @SuppressWarnings("MissingJavadoc")
    public Object getSubscribObject() {
        return subscribeObject;
    }

    @SuppressWarnings("MissingJavadoc")
    public void setDisable(boolean b) {
        this.disable = b;
    }

    @SuppressWarnings("MissingJavadoc")
    public boolean isDisable() {
        return disable;
    }

    @SuppressWarnings("MissingJavadoc")
    public Method getSubMethod() {
        return subscribeMethod;
    }
}
