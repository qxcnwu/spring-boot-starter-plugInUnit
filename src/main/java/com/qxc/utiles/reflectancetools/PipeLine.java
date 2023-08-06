package com.qxc.utiles.reflectancetools;

import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.CurrentSystemInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 11:14
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @param <T>
 */
@Data
@Slf4j
public class PipeLine<T extends CloneInstance<T>> {
    @SuppressWarnings("MissingJavadoc")
    public T instance;
    @SuppressWarnings("MissingJavadoc")
    public Class<?> clazz;
    private HashMap<String, MethodNode> map;
    private List<MethodNode> parseWhitExecCode;
    private ExecutorService pool;
    private static final ExecutorService EXECUTOR_SERVICE = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("PipeLineThreadPool", CurrentSystemInfo.getInstance().getCPU_Processors() * 2);

    /**
     * 单线程的线程池
     */
    private static final ExecutorService EXECUTOR = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("PipeLineThreadPool");
    private final boolean threadAble;

    /**
     * 初始化对象类
     *
     */
    @Contract(pure = true)
    public PipeLine(@NotNull T instance) {
        this(instance, false, EXECUTOR_SERVICE);
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PipeLine(@NotNull T instance, boolean threadAble) {
        this(instance, threadAble, EXECUTOR_SERVICE);
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PipeLine(@NotNull T instance, boolean threadAble, int number) {
        this(instance, threadAble, ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("PipeLineThreadPool", number));
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PipeLine(@NotNull T instance, boolean threadAble, ExecutorService pool) {
        this.instance = instance;
        clazz = instance.getClass();
        this.threadAble = threadAble;
        this.pool = pool;
        init();
    }

    /**
     * 初始化各种方法以及头文件
     */
    public void init() {
        // 获得所有方法，检查循环引用
        getAllMethod();
        // 标准化按顺序输出所有的MethodNode
        parseWhitExecCode = AnnotationTools.parse(map);
        // 标记头方法
        for (MethodNode mm : parseWhitExecCode) {
            if (mm.getExecProc() != 0) {
                break;
            }
            mm.setHead(true);
        }
    }


    /**
     * 各种方法按照顺序执行
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void exec(Object obj) throws ExecutionException, InterruptedException {
        T t = instance.clone();
        if (!threadAble) {
            // 使用单线程
            int idx = 0;
            Future<Boolean> submit = null;
            while (idx < parseWhitExecCode.size() && (submit == null || submit.isDone())) {
                submit = EXECUTOR.submit(getCallable(obj, idx, t));
                if (!submit.get()) {
                    throw new RuntimeException(parseWhitExecCode.get(idx) + " exec one thread warning");
                }
                ++idx;
            }
        } else {
            // 左侧索引
            int left = 0;
            int i = 1;
            // 使用多线程，相同计算优先级的同时执行
            for (; i < parseWhitExecCode.size(); i++) {
                if (parseWhitExecCode.get(i).getExecProc() != parseWhitExecCode.get(left).getExecProc()) {
                    CountDownLatch countDownLatch = new CountDownLatch(i - left);
                    for (int j = left; j < i; j++) {
                        pool.submit(getCallable(obj, j, countDownLatch, t));
                    }
                    countDownLatch.await();
                    left = i;
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(i - left);
            for (int j = left; j < parseWhitExecCode.size(); j++) {
                pool.submit(getCallable(obj, j, countDownLatch, t));
            }
            countDownLatch.await();
            log.info("pipeline has been update!!");
        }
    }

    /**
     * 获取可执行对象
     *
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    private @NotNull Callable<Boolean> getCallable(Object obj, int finalIdx, T t) {
        return () -> {
            final MethodNode methodTmp = parseWhitExecCode.get(finalIdx);
            final Method mTmp = methodTmp.getMethod();
            final RunState runAns;
            // 如果是head函数那么需要传入obj
            if (methodTmp.getParamNum() == 1) {
                // 查询参数类型正确
                if (!mTmp.getParameterTypes()[0].isAssignableFrom(obj.getClass())) {
                    throw new RuntimeException("Warning input param type");
                }
                runAns = (RunState) mTmp.invoke(t, obj);
            } else {
                // 否则不传入参数
                runAns = (RunState) mTmp.invoke(t);
            }
            return runAns != RunState.ERROR;
        };
    }


    /**
     * 获取可执行对象
     *
     */
    @Contract(value = "_, _, _,_ -> new", pure = true)
    private @NotNull Callable<Boolean> getCallable(Object obj, int finalIdx, CountDownLatch countDownLatch, T t) {
        return () -> {
            final MethodNode methodTmp = parseWhitExecCode.get(finalIdx);
            final Method mTmp = methodTmp.getMethod();
            final RunState runAns;
            // 如果是head函数那么需要传入obj
            if (methodTmp.getParamNum() == 1) {
                // 查询参数类型正确
                if (!mTmp.getParameterTypes()[0].isAssignableFrom(obj.getClass())) {
                    throw new RuntimeException("Warning input param type");
                }
                runAns = (RunState) mTmp.invoke(t, obj);
            } else {
                // 否则不传入参数
                runAns = (RunState) mTmp.invoke(t);
            }
            countDownLatch.countDown();
            return runAns != RunState.ERROR;
        };
    }


    /**
     * 获取所有执行方法
     */
    private void getAllMethod() {
        final Method[] methods = instance.getClass().getMethods();
        // 得到所有包含有@MethodOrder的对象,解析所有方法
        map = new HashMap<>(methods.length);
        int id = 0;
        for (Method method : methods) {
            /*
              方法需要具有注解、需要返回Runstate类型、需要输入参数小于1个
             */
            if (method.isAnnotationPresent(MethodOrder.class) && method.getReturnType().isAssignableFrom(RunState.class) && method.getParameterCount() <= 1) {
                map.put(method.getName(), new MethodNode(method, id));
                id++;
            }
        }
        if (!AnnotationTools.checkCircleReference(map)) {
            throw new RuntimeException("Method parse error!!");
        }
    }


    @SuppressWarnings("MissingJavadoc")
    @Data
    @Accessors(chain = true)
    @Slf4j
    public static class MethodNode {
        Method method;
        Class<?> clazz;
        String[] parents;
        List<String> sons = new ArrayList<>();
        String name;
        boolean isHead;
        int id;
        int paramNum;
        int pnum;
        int execProc;

        @SuppressWarnings("MissingJavadoc")
        public MethodNode() {

        }

        @SuppressWarnings("MissingJavadoc")
        public MethodNode(@NotNull Method method, int id) {
            this.method = method;
            final MethodOrder annotation = method.getAnnotation(MethodOrder.class);
            this.parents = annotation.methods();
            this.pnum = this.parents.length;
            this.name = method.getName();
            this.id = id;
            this.isHead = annotation.isHead();
            this.paramNum = method.getParameterCount();
            if (this.paramNum != 0) {
                this.clazz = method.getParameterTypes()[0];
            }
        }

        /**
         * 双向绑定
         *
         */
        public void add(String son) {
            sons.add(son);
        }

        /**
         * 引用计数减1
         */
        public void pnumDecrease() {
            pnum--;
        }

        @SuppressWarnings("MissingJavadoc")
        public Class<?> getClazz() {
            return clazz;
        }
    }
}