package com.qxc.utiles.reflectancetools;

import com.qxc.threadexector.documentthreadexector.MethodWithOrder;
import com.qxc.utiles.pluginunittools.CheckOrder;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 10:12
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 */
@Slf4j
public class MethodsUtiles {
    @SuppressWarnings("MissingJavadoc")
    public final static int MAX = CheckTypeEnum.values().length;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static @NotNull HashMap<CheckTypeEnum, PriorityQueue<MethodWithOrder>> makeCheckMap(ArrayList<Class<?>> arr) {
        HashMap<CheckTypeEnum, PriorityQueue<MethodWithOrder>> map = new HashMap<>(MAX);
        for (CheckTypeEnum tmp : CheckTypeEnum.values()) {
            map.put(tmp, new PriorityQueue<>(Comparator.comparingInt(MethodWithOrder::getOrder)));
        }
        // 遍历全部检查类
        for (Class<?> clazz : arr) {
            final ArrayList<MethodWithOrder> methodFromClazz = getMethodFromClazz(clazz);
            for (MethodWithOrder m : methodFromClazz) {
                int tCode = m.getTypeCode();
                for (int i = 1; i < MAX; i++) {
                    if ((tCode & (1 << i)) != 0) {
                        map.get(CheckTypeEnum.class.getEnumConstants()[i]).offer(m);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取全部clazz当中的所有方法
     *
     */
    @Contract(pure = true)
    public static @NotNull ArrayList<MethodWithOrder> getMethodFromClazz(@NotNull Class<?> clazz) {
        ArrayList<MethodWithOrder> map = new ArrayList<>();
        final CheckType annotation = clazz.getAnnotation(CheckType.class);
        final CheckTypeEnum[] check = annotation.check();
        final CheckTypeEnum[] uncheck = annotation.uncheck();
        // 如果内部包含全部选项那么我们进行转换,首先需要堆所有时间进行编码
        // AllDIR,PARENTDIR,DOCDIR,SERVERDIR,SRCDIR,STATICDIR,CONFIG
        // 分别是向左移动ordinal()位
        int initCode = 1 << MAX;
        initCode = makeCode(initCode, check, uncheck);
        final Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            // 首先检查方法的入参与返回值
            if (!checkMethod(m)) {
                continue;
            }
            final CheckType annotation1 = m.getAnnotation(CheckType.class);
            final CheckOrder annotation2 = m.getAnnotation(CheckOrder.class);
            int tpCode = initCode;
            if (annotation1 != null) {
                tpCode = makeCode(tpCode, annotation1.check(), annotation1.uncheck());
            }
            MethodWithOrder methodWithOrder = new MethodWithOrder().setMethod(m).setTypeCode(tpCode).setOrder(annotation2 == null ? -1 : annotation2.value()).setClazz(clazz);
            map.add(methodWithOrder);
        }
        return map;
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static int makeCode(int code, CheckTypeEnum @NotNull [] check, CheckTypeEnum[] uncheck) {
        // 对应的检查位置置为1，排除位置置为0
        for (CheckTypeEnum ce : check) {
            code = code | (1 << ce.ordinal());
        }
        // 如果具有排除位置那么除了排除位之外其他位置全部为1，当然第0位也为0
        // 这个生效的前提是第一位为1
        if ((code & 1) != 0) {
            code = 1 << MAX;
            for (int i = 1; i < MAX; i++) {
                code = code | (1 << i);
            }
        }
        for (CheckTypeEnum ce : uncheck) {
            code = code & (~(1 << ce.ordinal()));
        }
        return code;
    }

    @SuppressWarnings("MissingJavadoc")
    public static boolean checkMethod(@NotNull Method method) {
        // 输入参数需要为String
        // 输出参数需要为Boolean
        return method.getReturnType().isAssignableFrom(Boolean.class) && method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(String.class);
    }
}
