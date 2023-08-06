package com.qxc.threadexector.registerthreadexector;


import com.qxc.pluginunit.PlugInUnitDocument;
import com.qxc.pluginunit.PlugInUnitServers;
import com.qxc.threadexector.configuration.RegisterThreadExectorConfiguration;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 13:33
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.registerthreadexector
 */
@Component
@Slf4j
public class RegisterThreadExectorImpl implements RegisterThreadExector {

    private final ExecutorService pool;
    private final RegisterThreadExectorContext registerThreadExectorContext;
    private boolean start = true;

    @SuppressWarnings("MissingJavadoc")
    public RegisterThreadExectorImpl(@NotNull RegisterThreadExectorConfiguration configuration, RegisterThreadExectorContext registerThreadExectorContext) {
        pool = configuration.isMultiThread() ? ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("RegisterThreadExectorThreadPool", Math.max(1, configuration.getMultiThreadNumber())) : ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("RegisterThreadExectorThreadPool");
        this.registerThreadExectorContext = registerThreadExectorContext;
    }


    /**
     * 提交一个工件
     *
     */
    @Override
    public void submit(NewPlugInUnitWithContext newPlugInUnitWithContext) {
        if (start && !pool.isShutdown()) {
            final Future<Boolean> future = pool.submit(new NewPlugInUnitWithContextCallable(newPlugInUnitWithContext));
            newPlugInUnitWithContext.setFuture(future);
        }
    }

    /**
     * 回退功能版本
     *
     */
    @Override
    public void rollback(NewPlugInUnitWithContext newPlugInUnitWithContext) {

    }

    @Override
    public boolean shutdown() {
        if (start && !pool.isShutdown()) {
            start = false;
            pool.shutdown();
        }
        return pool.isShutdown();
    }

    private class NewPlugInUnitWithContextCallable implements Callable<Boolean> {
        private final NewPlugInUnitWithContext context;

        @Contract(pure = true)
        public NewPlugInUnitWithContextCallable(NewPlugInUnitWithContext context) {
            this.context = context;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         */
        @Contract(pure = true)
        @Override
        public @Nullable Boolean call() {
            // 首先检查文件要求是否满足
            final Future<Boolean> checked = registerThreadExectorContext.getCheckThreadExector().submit(context.getPlugInUnit());
            try {
                // 检查文件是否满足要求
                if (!checked.get()) {
                    return false;
                }
                // 开启服务
                final PlugInUnitServers servers = registerThreadExectorContext.getPlugInUnitServerFactory().initServers(context.getPlugInUnit(), context.isLatest());
                context.setPlugInUnitServers(servers);
                log.error(servers.toString() + " " + context.isLatest());
                // 更新文档
                final PlugInUnitDocument documents = registerThreadExectorContext.getPlugInUnitDocumentFactory().getPlugInUnitDocument(context.getPlugInUnit());
                context.setPlugInUnitDocument(documents);
                // 这里就不再进行跟新了，通知数据库工厂进行更新，在注册环节已经更新
                // registerThreadExectorContext.getEventBus().post(context, "registry success");
                return true;
            } catch (InterruptedException | ExecutionException e) {
                log.info("Wrong registry plugInUnit:" + e.getMessage());
                return false;
            }
        }
    }
}
