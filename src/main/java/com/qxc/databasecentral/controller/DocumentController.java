package com.qxc.databasecentral.controller;

import com.qxc.databasecentral.pojo.Result;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import com.qxc.threadexector.registrationcentercore.RegisterCenterFactory;
import com.qxc.utiles.containertools.HashMapWithNodeImpl;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/16 23:44
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.controller
 */
@RestController
@RequestMapping("/document")
public class DocumentController {
    private final RegisterCenterFactory registerCenterFactory;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public DocumentController(RegisterCenterFactory registerCenterFactory) {
        this.registerCenterFactory = registerCenterFactory;
    }

    /**
     * 刷新全部文档
     *
     */
    @Contract(pure = true)
    @RequestMapping("/refresh")
    private Result refresh() {
        registerCenterFactory.refreshDoc();
        return new Result().setData(true).setFinish(true).setMessage("refresh now");
    }

    /**
     * 刷新全部文档
     *
     */
    @Contract(pure = true)
    @RequestMapping("/updateallpluginunit")
    private Result update() {
        registerCenterFactory.getAll(false).forEach(a -> a.getContext().getPlugInUnitDocument().updateMarkdown());
        return new Result().setData(true).setFinish(true).setMessage("update all pluginunits");
    }

    /**
     * 更新对应插件的最新文档
     *
     */
    @Contract(pure = true)
    @RequestMapping("/update/{servername}")
    private Result updateLatestByName(@PathVariable String servername) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(servername);
        // 如果当前为空那么需要刷新整个文档
        if (latest == null) {
            registerCenterFactory.refreshDoc();
            return new Result().setData(false).setFinish(true).setMessage("no such pluginunit");
        }
        latest.getPlugInUnitDocument().updateMarkdown();
        return new Result().setData(true).setFinish(true).setMessage("refresh success");
    }

    /**
     * 通过插件名称以及版本来更新相应的文档
     *
     */
    @Contract(pure = true)
    @RequestMapping("/update/{servername}/{version}")
    private Result updateByNameByVersion(@PathVariable String servername, @PathVariable String version) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getByName(servername, version).getContext();
        // 如果当前为空那么需要刷新整个文档
        if (latest == null) {
            registerCenterFactory.refreshDoc();
            return new Result().setData(false).setFinish(true).setMessage("no such pluginunit");
        }
        latest.getPlugInUnitDocument().updateMarkdown();
        return new Result().setData(true).setFinish(true).setMessage("refresh success");
    }

    /**
     * 更新对应名称的所有文档
     *
     */
    @Contract(pure = true)
    @RequestMapping("/updateall/{servername}")
    private Result updateByName(@PathVariable String servername) {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> pNAll = registerCenterFactory.getAll(servername);
        for (HashMapWithNodeImpl.PlugInUnitContextWithNode pn : pNAll) {
            pn.getContext().getPlugInUnitDocument().updateMarkdown();
        }
        return new Result().setData(pNAll.size()).setFinish(true).setMessage("refresh success");
    }
}
