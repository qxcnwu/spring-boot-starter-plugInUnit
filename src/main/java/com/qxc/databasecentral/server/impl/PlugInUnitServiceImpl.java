package com.qxc.databasecentral.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxc.databasecentral.dao.PlugInUnitDao;
import com.qxc.databasecentral.pojo.PlugInUnitPojo;
import com.qxc.databasecentral.server.PlugInUnitService;
import com.qxc.threadexector.eventbus.Subscribe;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 19:50
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.server
 */
@Service
@Slf4j
public class PlugInUnitServiceImpl extends ServiceImpl<PlugInUnitDao, PlugInUnitPojo> implements PlugInUnitService {

    private final PlugInUnitDao plugInUnitDao;

    @SuppressWarnings("MissingJavadoc")
    @Autowired
    public PlugInUnitServiceImpl(PlugInUnitDao plugInUnitDao) {
        this.plugInUnitDao = plugInUnitDao;
    }

    /**
     * 添加新的小组件
     *
     */
    @Override
    @Subscribe(topic = {"registry success"})
    public boolean insertNewPlugInUnit(@NotNull NewPlugInUnitWithContext plug) {
        QueryWrapper<PlugInUnitPojo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("pluginunit", plug.getPlugInUnitName()).eq("version", plug.getVersionName());
        if (plugInUnitDao.selectList(wrapper1).size()!=0) {
            return false;
        }
        UpdateWrapper<PlugInUnitPojo> wrapper = new UpdateWrapper<>();
        wrapper.eq("pluginunit", plug.getPlugInUnitName()).set("islatest", false);
        plugInUnitDao.update(null, wrapper);
        log.info("registry success " + plug);
        return plugInUnitDao.insert(new PlugInUnitPojo(plug.getPlugInUnitName(), plug.getVersionName(), plug.getRootPath())) > 0;
    }

    @Override
    @Subscribe(topic = {"scan pluginunit"})
    public boolean insertScanNewPlugInUnit(@NotNull NewPlugInUnitWithContext plug) {
        QueryWrapper<PlugInUnitPojo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("pluginunit", plug.getPlugInUnitName()).eq("version", plug.getVersionName());
        if (plugInUnitDao.selectList(wrapper1).size()!=0) {
            return false;
        }
        QueryWrapper<PlugInUnitPojo> wrapper = new QueryWrapper<>();
        wrapper.eq("pluginunit", plug.getPlugInUnitName()).eq("version", plug.getVersionName());
        final PlugInUnitPojo plugInUnitPojo = plugInUnitDao.selectOne(wrapper);
        if (plugInUnitPojo != null) {
            return true;
        }
        log.info("scan pluginunit " + plug);
        return plugInUnitDao.insert(new PlugInUnitPojo(plug.getPlugInUnitName(), plug.getVersionName(), plug.getRootPath())) > 0;
    }

    /**
     * 初始化
     *
     */
    @Override
    @Subscribe(topic = {"setLatest"})
    public boolean setLatest(@NotNull PlugInUnitPojo pojo) {
        UpdateWrapper<PlugInUnitPojo> wrapper = new UpdateWrapper<>();
        wrapper.eq("pluginunit", pojo.getPluginunit()).set("islatest", false);
        plugInUnitDao.update(null, wrapper);
        wrapper = new UpdateWrapper<>();
        wrapper.eq("pluginunit", pojo.getPluginunit()).eq("pluginunit", pojo.getVersion()).set("islatest", true);
        log.info("setLatest " + pojo);
        return plugInUnitDao.update(null, wrapper) > 0;
    }

    /**
     * 查询当前表中所有的小组件
     *
     */
    @Override
    public List<PlugInUnitPojo> getAllPlugInUnit() {
        final QueryWrapper<PlugInUnitPojo> objectQueryWrapper = new QueryWrapper<>();
        return plugInUnitDao.selectList(objectQueryWrapper.ge("id", -1));
    }

    /**
     * 根据组件名称获取对应的最新的组件
     *
     */
    @Override
    public PlugInUnitPojo getLatestVersion(String plName) {
        QueryWrapper<PlugInUnitPojo> wrapper = new QueryWrapper<>();
        wrapper.eq("pluginunit", plName);
        return plugInUnitDao.selectOne(wrapper);
    }

    /**
     * 获取所有最新的版本
     *
     */
    @Override
    public List<PlugInUnitPojo> getLatestVersion() {
        QueryWrapper<PlugInUnitPojo> wrapper = new QueryWrapper<>();
        wrapper.eq("islatest", true);
        return plugInUnitDao.selectList(wrapper);
    }
}
