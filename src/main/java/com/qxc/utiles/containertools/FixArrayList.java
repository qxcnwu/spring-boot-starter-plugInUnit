package com.qxc.utiles.containertools;

import com.qxc.pluginunit.PlugInUnitDomain;

/**
 * @Author qxc
 * @Date 2023 2023/7/6 16:32
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @param <T>
 */
public interface FixArrayList<T extends PlugInUnitDomain> {
    /**
     * 在最后添加，设置名称
     *
     */
    boolean add(String version, T obj);

    /**
     * 在指定位置处添加T对象并使用默认名称
     *
     */
    boolean add(String version, T obj, int index);

    /**
     * 删除T对象
     *
     */
    boolean remove(T obj);

    /**
     * 删除对应位置出的对象
     *
     */
    boolean remove(int index);

    /**
     * 删除指定名称的对象
     *
     */
    boolean remove(String version);

    /**
     * 更新index处的对象
     *
     */
    boolean update(int index, T obj);

    /**
     * 更新名称为version的对象
     *
     */
    boolean update(String version, T obj);

    /**
     * 查找对应的对象
     * @param fromAll fromAll
     * @param obj obj
     * @return boolean
     */
    boolean conatins(T obj,boolean fromAll);

    /**
     * 查找对象
     * @param fromAll fromAll
     * @param version version
     * @return boolean
     */
    boolean conatins(String version,boolean fromAll);

    /**
     * 查找对象
     *
     */
    boolean conatins(int index);

    /**
     * 获取对象
     *
     */
    T getObject(String version);

    /**
     * 当前对象获取
     *
     */
    T getObject(int index);

    /**
     * 当前对象获取
     *
     */
    T getCurObject();

    /**
     * 得到当前的剩余容量
     *
     */
    int getLeaveSize();

    /**
     * 得到当前已经使用的容量
     *
     */
    int getSize();

    /**
     * 得到容量
     *
     */
    int getCapacity();


    /**
     * 是否已经满了
     *
     */
    boolean isFull();

    /**
     * 获取当前的缓存容量
     *
     */
    int getTempSize();

    /**
     * 容量满足后的策略
     *
     */
    RefuseStrategies<T> getRefuseStrategies();
}
