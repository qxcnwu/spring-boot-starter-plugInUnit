package com.qxc.threadexector;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:16
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.construct
 */
public interface DenyPolice {
    /**
     * 拒绝策略
     */
    void rejectedExecution(Runnable runnable, ThreadPoolManage threadPool);

    @SuppressWarnings("MissingJavadoc")
    class DiscardDenyPolice implements DenyPolice {

        /**
         * Method that may be invoked by a {@link ThreadPoolExecutor} when
         * {@link ThreadPoolExecutor#execute execute} cannot accept a
         * task.  This may occur when no more threads or queue slots are
         * available because their bounds would be exceeded, or upon
         * shutdown of the Executor.
         *
         * <p>In the absence of other alternatives, the method may throw
         * an unchecked {@link RejectedExecutionException}, which will be
         * propagated to the caller of {@code execute}.
         *
         * @param r        the runnable task requested to be executed
         * @param executor the executor attempting to execute this task
         * @throws RejectedExecutionException if there is no remedy
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolManage executor) {
            // do nothing
        }
    }

    @SuppressWarnings("MissingJavadoc")
    class AbortDenyPolice implements DenyPolice {

        /**
         * Method that may be invoked by a {@link ThreadPoolExecutor} when
         * {@link ThreadPoolExecutor#execute execute} cannot accept a
         * task.  This may occur when no more threads or queue slots are
         * available because their bounds would be exceeded, or upon
         * shutdown of the Executor.
         *
         * <p>In the absence of other alternatives, the method may throw
         * an unchecked {@link RejectedExecutionException}, which will be
         * propagated to the caller of {@code execute}.
         *
         * @param r        the runnable task requested to be executed
         * @param executor the executor attempting to execute this task
         * @throws RejectedExecutionException if there is no remedy
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolManage executor) {
            throw new RuntimeException(r.toString() + " will be abort");
        }
    }

    @SuppressWarnings("MissingJavadoc")
    class RunnerDenyPolice implements DenyPolice {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolManage executor) {
            if (!executor.isShutDown()) {
                r.run();
            }
        }
    }
}
