package com.qxc.initializerbean;

import com.qxc.configuration.BaseConfiguration;
import com.qxc.threadexector.documentthreadexector.MethodWithOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import com.qxc.utiles.reflectancetools.ClassUtiles;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static com.qxc.utiles.reflectancetools.MethodsUtiles.makeCheckMap;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 18:56
 * @Version 1.0
 * @PACKAGE com.qxc.initializerbean
 */
@Component
public class AllClassWithCheckTypeAnnotation {
    @Getter
    final ArrayList<Class<?>> allClassWithAnnotation;
    @Getter
    final HashMap<CheckTypeEnum, PriorityQueue<MethodWithOrder>> checkTypeEnumPriorityQueueHashMap;

    @SuppressWarnings("MissingJavadoc")
    @Autowired
    public AllClassWithCheckTypeAnnotation(@NotNull BaseConfiguration baseConfiguration) throws IOException, ClassNotFoundException {
        allClassWithAnnotation = ClassUtiles.getAllClassWithAnnotation(baseConfiguration.getBasePackage(), CheckType.class);
        checkTypeEnumPriorityQueueHashMap = makeCheckMap(allClassWithAnnotation);
    }
}
