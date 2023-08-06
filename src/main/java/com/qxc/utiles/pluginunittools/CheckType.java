package com.qxc.utiles.pluginunittools;

import java.lang.annotation.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/5 23:55
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckType {
    /**
     * doc
     */
    int value() default 1;

    /**
     * 当前注解用于标注检查方法目标对象
     */
    CheckTypeEnum[] check() default CheckTypeEnum.AllDIR;

    /**
     * 用于标注无需检查的部分
     *
     */
    CheckTypeEnum[] uncheck() default CheckTypeEnum.AllDIR;
}
