package com.qxc.databasecentral.controller;

import com.qxc.databasecentral.pojo.Result;
import com.qxc.pluginunit.PlugInUnitServer;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import com.qxc.threadexector.registrationcentercore.RegisterCenterFactory;
import com.qxc.utiles.containertools.HashMapWithNodeImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 14:22
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.controller
 */
@RestController
@RequestMapping("/serverlog")
public class ServerLogController {
    private final RegisterCenterFactory registerCenterFactory;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public ServerLogController(RegisterCenterFactory registerCenterFactory) {
        this.registerCenterFactory = registerCenterFactory;
    }

    /**
     * 获取某一个插件某一版本的全部日志
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getlog/{pluginunitname}/{versionname}")
    private Result getLatestLogByNameWithServer(@PathVariable String pluginunitname, @PathVariable String versionname, OutType outType) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final HashMap<String, HashMap<String, String>> stringHashMapHashMap = orderLog(outType, context.getPlugInUnitServers().getAllServers());
        return new Result().setData(stringHashMapHashMap).setFinish(true).setMessage("get service logs" + pluginunitname);
    }

    /**
     * 获取某一个插件某一版本对应服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getlog/{pluginunitname}/{versionname}/{servername}")
    private Result openServerByNameWithVersion(@PathVariable String pluginunitname, @PathVariable String versionname, @PathVariable String servername, OutType outType) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        // 获取特定版本的服务
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final PlugInUnitServer openServer = context.getPlugInUnitServers().containServer(servername);
        if (openServer == null) {
            return new Result().setData(null).setFinish(true).setMessage("no such server");
        }
        final HashMap<String, HashMap<String, String>> stringHashMapHashMap = orderLog(outType, openServer);
        return new Result().setData(stringHashMapHashMap).setFinish(true).setMessage("open service " + openServer + " " + pluginunitname + " " + servername);
    }

    /**
     * 获取对应插件的最新版本的服务的全部日志
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getlog/{pluginunitname}")
    private Result openLatestServerByName(@PathVariable String pluginunitname, OutType outType) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        final HashMap<String, HashMap<String, String>> stringHashMapHashMap = orderLog(outType, latest.getPlugInUnitServers().getAllServers());
        return new Result().setData(stringHashMapHashMap).setFinish(true).setMessage("open latest service " + pluginunitname);
    }

    /**
     * 获取全部相应的消息
     *
     */
    private static @NotNull HashMap<String, HashMap<String, String>> orderLog(OutType outType, PlugInUnitServer @NotNull ... plugInUnitServers) {
        HashMap<String, HashMap<String, String>> map = new HashMap<>(plugInUnitServers.length);
        for (PlugInUnitServer openServer : plugInUnitServers) {
            HashMap<String, String> mapTemp = new HashMap<>(1 + outType.ordinal() / 2);
            switch (outType) {
                case ALL:
                    mapTemp.put("outStream", openServer.getOutputStream(false));
                    mapTemp.put("outErrStream", openServer.getOutputStream(true));
                    break;
                case ERR:
                    mapTemp.put("outErrStream", openServer.getOutputStream(true));
                    break;
                default:
                    mapTemp.put("outStream", openServer.getOutputStream(false));
                    break;
            }
            map.put(openServer.getServerName(), mapTemp);
        }
        return map;
    }


    /**
     * 查询日志的枚举类型
     */
    private enum OutType {
        /**
         * ERR是输出错误信息
         * STE输出正常的消息
         * ALL消息全部输出
         */
        ERR, STE, ALL
    }
}
