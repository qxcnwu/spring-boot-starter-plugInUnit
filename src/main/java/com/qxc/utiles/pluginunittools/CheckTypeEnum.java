package com.qxc.utiles.pluginunittools;

/**
 * @Author qxc
 * @Date 2023 2023/7/5 23:59
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 */
public enum CheckTypeEnum {
    /**
     * AllDIR 检查所有对象
     * PARENTDIR 检查父文件夹
     * DOCDIR 检查文档目录
     * SERVERDIR 检查服务目录
     * SRCDIR 检查核心功能目录
     * STATICDIR 检查静态资源目录
     * CONFIG 检查配置文件
     */
    AllDIR, @SuppressWarnings("MissingJavadoc") PARENTDIR, @SuppressWarnings("MissingJavadoc") DOCDIR, @SuppressWarnings("MissingJavadoc") SERVERDIR, @SuppressWarnings("MissingJavadoc") SRCDIR, @SuppressWarnings("MissingJavadoc") STATICDIR, @SuppressWarnings("MissingJavadoc") CONFIG
}
