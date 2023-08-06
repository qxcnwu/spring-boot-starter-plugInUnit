package com.qxc.utiles.filesystemtools;

/**
 * @Author qxc
 * @Date 2023 2023/7/9 15:55
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @param <T>
 */
public interface FileCheckable<T> {
    /**
     * 判断目录是否满足条件
     */
    boolean check(T path);
}
