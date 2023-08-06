package com.qxc.threadexector.documentthreadexector;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 11:21
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.documentthreadexector
 */
@Data
@Accessors(chain = true)
public class MethodWithOrder {
    Method method;
    int order;
    int typeCode;
    Class<?> clazz;
}
