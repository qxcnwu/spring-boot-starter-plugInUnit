package com.qxc.utiles.containertools;

import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 22:47
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.containertools
 */
public interface HashMapWithNode {
    /*
      具备增删改查的功能
      需要在ON的时间当中获取所有版本
      在O(1)的时间中获得当前的标记位置
      实现增删改查
      HashMap<String,LinedList-hasNode>
     */

    /**
     * 插入plug
     *
     */
    void insert(NewPlugInUnitWithContext plug);

    /**
     * 插入对应位置是否为最新的插件
     *
     */
    void insert(@NotNull NewPlugInUnitWithContext plug, boolean isLatest);

    /**
     * 删除对应的对象
     *
     */
    void delete(String pName, String version);

    /**
     * 删除对象
     *
     */
    void delete(NewPlugInUnitWithContext plug);

    /**
     * 查询对应的对象是否存在
     *
     * @return
     */
    boolean contains(NewPlugInUnitWithContext plug);

    /**
     * 查询
     *
     * @return
     */
    boolean contains(String pName, String version);


    /**
     * 是否是最新的插件
     *
     */
    boolean isLatest(NewPlugInUnitWithContext plug);

    /**
     * 是否是最新的插件
     *
     * @param pName 插件名称
     * @param version 插件版本
     * @return 是否为最新插件
     */
    boolean isLatest(String pName, String version);

    /**
     * 将这个值设置为当前版本
     *
     */
    boolean setLatest(String pName, String version);

    /**
     * 通过插件名称以及版本获取对象
     *
     */
    HashMapWithNodeImpl.PlugInUnitContextWithNode getByName(String name, String version);

    /**
     * 获取当前执行插件
     *
     */
    NewPlugInUnitWithContext getLatest(String name);

    /**
     * 获取全部插件
     */
    List<HashMapWithNodeImpl.PlugInUnitContextWithNode> getAll(boolean latest);

    /**
     * 获取全部插件的全部版本
     */
    List<HashMapWithNodeImpl.PlugInUnitContextWithNode> getAll(String pName);

    /**
     * 清除容器内部所有的对象
     */
    void clear();
}
