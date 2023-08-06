package com.qxc.utiles.reflectancetools;

import java.lang.annotation.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 11:21
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MethodOrder {
    @SuppressWarnings("MissingJavadoc") String[] value() default {};

    /**
     * 完成前置的方法
     *
     */
    String[] methods() default {};

    /**
     * 是否是头部文件
     *
     */
    boolean isHead() default true;
}
