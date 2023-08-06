package com.qxc.threadexector.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 9:55
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.eventbus
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    /**
     * 方法上的注解指定需要监听的事件
     * @return
     */
    String[] topic() default {"default-topic"};
}
