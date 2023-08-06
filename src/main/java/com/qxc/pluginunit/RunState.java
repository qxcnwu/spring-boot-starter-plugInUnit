package com.qxc.pluginunit;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 23:58
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit
 */
public enum RunState {
    /**
     * PERPARE: 检查完成可以运行
     * WAITING: 检查完成可以运行
     * RUNNING: 正在运行
     * DONE: 运行完成
     * ERROR: 运行错误
     * STOP: 运行正常终止
     */
    PERPARE, @SuppressWarnings("MissingJavadoc") WAITING, @SuppressWarnings("MissingJavadoc") RUNNING, @SuppressWarnings("MissingJavadoc") DONE, @SuppressWarnings("MissingJavadoc") ERROR, @SuppressWarnings("MissingJavadoc") STOP
}
