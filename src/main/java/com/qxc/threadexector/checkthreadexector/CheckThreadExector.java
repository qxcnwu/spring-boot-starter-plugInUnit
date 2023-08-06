package com.qxc.threadexector.checkthreadexector;

import com.qxc.pluginunit.construct.PlugInUnit;
import com.qxc.threadexector.configuration.CheckThreadExectorConfiguration;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 23:39
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.checkthreadexector
 * @description 会自动执行，需要获取线程的时候能够通过自动装配，进行提交
 */
@Component
@Slf4j
public class CheckThreadExector {

    private final ExecutorService pool;
    private final HashMap<CheckTypeEnum, CheckExectorFactory> factoryHashMap;

    @SuppressWarnings("MissingJavadoc")
    public static final int NUMBER = CheckTypeEnum.values().length;

    /**
     * factory导入
     *
     */
    @Autowired
    public CheckThreadExector(@NotNull CheckThreadExectorConfiguration configuration, HashMap<CheckTypeEnum, CheckExectorFactory> factoryHashMap) {
        pool = configuration.isThread() ? ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("CheckThreadExectorThreadPool", configuration.getThreadNum()) : ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("CheckThreadExectorThreadPool");
        this.factoryHashMap = factoryHashMap;
    }

    /**
     * 提交对应的检查线程
     *
     */
    public Future<Boolean> submit(PlugInUnit plug) {
        final Callable<Boolean> booleanCallable = makeCall(plug);
        return pool.submit(booleanCallable);
    }

    /**
     * 创建Callable对象，通过Callable对象获取结果，同时提交到容器中执行
     *
     */
    @Contract(pure = true)
    private @NotNull Callable<Boolean> makeCall(PlugInUnit plug) {
        return () -> {
            CountDownLatch countDownLatch = new CountDownLatch(NUMBER);
            ArrayList<Future<Boolean>> ans = new ArrayList<>();
            for (CheckTypeEnum checkTypeEnum : factoryHashMap.keySet()) {
                ans.add(factoryHashMap.get(checkTypeEnum).add(plug.getByCheckType(checkTypeEnum), countDownLatch));
            }
            countDownLatch.await();
            for (Future<Boolean> tp : ans) {
                if (!tp.get()) {
                    return false;
                }
            }
            return true;
        };
    }

}
