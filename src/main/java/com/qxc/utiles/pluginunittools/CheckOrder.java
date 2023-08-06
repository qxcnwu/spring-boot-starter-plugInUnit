package com.qxc.utiles.pluginunittools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 15:18
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 * 检查等级
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckOrder{
    @SuppressWarnings("MissingJavadoc") int value();
}
