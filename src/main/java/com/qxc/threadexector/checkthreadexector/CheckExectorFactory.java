package com.qxc.threadexector.checkthreadexector;

import com.qxc.threadexector.documentthreadexector.MethodWithOrder;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 23:43
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.checkthreadexector
 */
@Data
@Slf4j
public class CheckExectorFactory {
    private final boolean isThread;
    private final ExecutorService pool;
    private final int threadNum;
    private final CheckTypeEnum checkTypeEnum;

    private final ArrayList<MethodWithOrder> arr = new ArrayList<>();
    private final HashMap<Class<?>, Object> instance = new HashMap<>();

    /**
     * 创建对应的工厂
     *
     * @param queue
     */
    @Contract(pure = true)
    public CheckExectorFactory(CheckTypeEnum checkTypeEnum, boolean isThread, int threadNum, ExecutorService pool, @NotNull PriorityQueue<MethodWithOrder> queue) {
        this.isThread = isThread;
        this.threadNum = threadNum;
        this.pool = pool;
        this.checkTypeEnum = checkTypeEnum;
        // 获取当中所有的检查方法
        while (!queue.isEmpty()) {
            final MethodWithOrder mwo = queue.poll();
            arr.add(mwo);
            final Class<?> clazz = mwo.getClazz();
            if (!instance.containsKey(clazz)) {
                // 创建执行对象
                instance.put(clazz, getInstance(clazz));
            }
        }
    }

    @SuppressWarnings("MissingJavadoc")
    public CheckExectorFactory(CheckTypeEnum checkTypeEnum, int threadNum, PriorityQueue<MethodWithOrder> queue) {
        this(checkTypeEnum, true, threadNum, ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("CheckExectorFactoryThreadPool",threadNum), queue);
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public CheckExectorFactory(CheckTypeEnum checkTypeEnum, PriorityQueue<MethodWithOrder> queue) {
        this(checkTypeEnum, false, 1, ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("CheckExectorFactoryThreadPool"), queue);
    }

    @SuppressWarnings("MissingJavadoc")
    public CheckExectorFactory(CheckTypeEnum checkTypeEnum, boolean thread, int threadNum, @NotNull PriorityQueue<MethodWithOrder> queue) {
        this.isThread = thread;
        this.threadNum = threadNum;
        this.pool = isThread ? ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("CheckExectorFactoryThreadPool",threadNum) : ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("CheckExectorFactoryThreadPool");
        this.checkTypeEnum = checkTypeEnum;
        // 获取当中所有的检查方法
        while (!queue.isEmpty()) {
            final MethodWithOrder mwo = queue.poll();
            arr.add(mwo);
            final Class<?> clazz = mwo.getClazz();
            if (!instance.containsKey(clazz)) {
                // 创建执行对象
                instance.put(clazz, getInstance(clazz));
            }
        }
    }

    private boolean exec(String path) {
        for (MethodWithOrder mwo : arr) {
            try {
                if (!((Boolean) mwo.getMethod().invoke(instance.get(mwo.getClazz()), path))) {
                   log.error(mwo.getMethod().getName()+" warning "+path+" "+checkTypeEnum);
                    return false;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("Run check method failure {}", mwo.getMethod().getName());
                return false;
            }
        }
        return true;
    }


    @SuppressWarnings("MissingJavadoc")
    public Future<Boolean> add(String path, CountDownLatch countDownLatch) {
        Callable<Boolean> booleanCallable = () -> {
            // 监测是否满足要求
            final boolean pass = exec(path);
            countDownLatch.countDown();
            return pass;
        };
        return pool.submit(booleanCallable);
    }

    /**
     * 创建执行对象
     *
     */
    @Contract(pure = true)
    private static @Nullable Object getInstance(@NotNull Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Create object failure!! {}", e.getMessage());
            return null;
        }
    }
}
