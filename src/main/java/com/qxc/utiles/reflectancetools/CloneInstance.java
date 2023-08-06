package com.qxc.utiles.reflectancetools;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 16:59
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.pluginunittools
 * @param <T>
 */
public interface CloneInstance<T> extends Cloneable{
    /**
     * 实现克隆接口
     * @return 克隆对象
     */
    T clone();
}
