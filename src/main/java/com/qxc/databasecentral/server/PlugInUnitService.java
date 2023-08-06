package com.qxc.databasecentral.server;

import com.qxc.databasecentral.pojo.PlugInUnitPojo;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 19:53
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.server
 */
public interface PlugInUnitService {
    /**
     * 添加新的小组件
     *
     */
    boolean insertNewPlugInUnit(NewPlugInUnitWithContext plug);

    /**
     * 初始化将没有加载进入数据库中的文件进行写入
     *
     */
    boolean insertScanNewPlugInUnit(@NotNull NewPlugInUnitWithContext plug);

    /**
     * 初始化
     */
    boolean setLatest(PlugInUnitPojo pojo);

    /**
     * 查询当前表中所有的小组件
     *
     */
    List<PlugInUnitPojo> getAllPlugInUnit();

    /**
     * 根据组件名称获取对应的最新的组件
     *
     */
    PlugInUnitPojo getLatestVersion(String plName);

    /**
     * 获取所有最新的版本
     *
     */
    List<PlugInUnitPojo> getLatestVersion();
}
