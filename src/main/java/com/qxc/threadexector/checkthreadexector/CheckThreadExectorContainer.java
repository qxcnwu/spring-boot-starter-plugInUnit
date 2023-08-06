package com.qxc.threadexector.checkthreadexector;

import com.qxc.initializerbean.AllClassWithCheckTypeAnnotation;
import com.qxc.threadexector.configuration.CheckThreadExectorConfiguration;
import com.qxc.threadexector.documentthreadexector.MethodWithOrder;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 15:26
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.checkthreadexector
 */
@Component
public class CheckThreadExectorContainer {
    private final Factory factory;
    private final AllClassWithCheckTypeAnnotation allClassWithCheckTypeAnnotation;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public CheckThreadExectorContainer(Factory factory, AllClassWithCheckTypeAnnotation allClassWithCheckTypeAnnotation) {
        this.factory = factory;
        this.allClassWithCheckTypeAnnotation = allClassWithCheckTypeAnnotation;
    }

    /**
     * 注册所有CheckTypeEnum对象的监测工厂
     */
    @Bean
    public HashMap<CheckTypeEnum, CheckExectorFactory> getAllEnumFactory() {
        HashMap<CheckTypeEnum, CheckExectorFactory> map = new HashMap<>(CheckTypeEnum.values().length);
        for (CheckTypeEnum checkTypeEnum : CheckTypeEnum.values()) {
            map.put(checkTypeEnum, factory.getFactory(checkTypeEnum, allClassWithCheckTypeAnnotation.getCheckTypeEnumPriorityQueueHashMap().get(checkTypeEnum)));
        }
        return map;
    }
}

@Component
class Factory {
    private final CheckThreadExectorConfiguration configuration;
    private final HashMap<CheckTypeEnum, CheckExectorFactory> concurrentHashMap = new HashMap<>();

    @Contract(pure = true)
    @Autowired
    public Factory(CheckThreadExectorConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 单例获取CheckExectorFactory
     *
     */
    public CheckExectorFactory getFactory(CheckTypeEnum checkTypeEnum, PriorityQueue<MethodWithOrder> queue) {
        if (!concurrentHashMap.containsKey(checkTypeEnum)) {
            synchronized (Factory.class) {
                if (!concurrentHashMap.containsKey(checkTypeEnum)) {
                    final CheckExectorFactory checkExectorFactory = new CheckExectorFactory(checkTypeEnum, configuration.isThread(), configuration.getThreadNum(), queue);
                    concurrentHashMap.put(checkTypeEnum, checkExectorFactory);
                    return checkExectorFactory;
                }
                return concurrentHashMap.get(checkTypeEnum);
            }
        }
        return concurrentHashMap.get(checkTypeEnum);
    }
}
