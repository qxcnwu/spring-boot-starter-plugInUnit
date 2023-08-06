package com.qxc.databasecentral.controller;

import com.qxc.databasecentral.pojo.Result;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import com.qxc.threadexector.registrationcentercore.RegisterCenterFactory;
import com.qxc.utiles.containertools.HashMapWithNodeImpl;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/16 17:12
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.controller
 */
@RestController
@RequestMapping("/central")
public class RegistryController {
    private final RegisterCenterFactory registerCenterFactory;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public RegistryController(RegisterCenterFactory registerCenterFactory) {
        this.registerCenterFactory = registerCenterFactory;
    }

    /**
     * 获取当前全部的插件
     *
     */
    @RequestMapping("/getall")
    public Result getAllPlugInUnit() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(false);
        List<NewPlugInUnitWithContext.NewPlugInUnitWithContextInner> arr = new ArrayList<>();
        for (HashMapWithNodeImpl.PlugInUnitContextWithNode pn : plugInUnitContextWithNodes) {
            arr.add(pn.getContext().translate());
        }
        return new Result().setData(arr).setFinish(true).setMessage("done");
    }



}
