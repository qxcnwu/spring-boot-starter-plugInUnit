package com.qxc.databasecentral.controller;

import com.qxc.databasecentral.pojo.Result;
import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.registerthreadexector.NewPlugInUnitWithContext;
import com.qxc.threadexector.registrationcentercore.RegisterCenterFactory;
import com.qxc.utiles.containertools.HashMapWithNodeImpl;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/17 19:23
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.controller
 */
@RestController
@RequestMapping("/server")
public class ServerController {
    private final RegisterCenterFactory registerCenterFactory;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public ServerController(RegisterCenterFactory registerCenterFactory) {
        this.registerCenterFactory = registerCenterFactory;
    }

    /**
     * 开启某一个插件的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/openall/{pluginunitname}/{versionname}")
    private Result openAllServerByName(@PathVariable String pluginunitname, @PathVariable String versionname) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final boolean openServer = context.getPlugInUnitServers().openAllServer();
        return new Result().setData(openServer).setFinish(true).setMessage("open service " + openServer + " " + pluginunitname);
    }

    /**
     * 开启对应插件的对应服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/open/{pluginunitname}/{versionname}/{servername}")
    private Result openServerByNameWithVersion(@PathVariable String pluginunitname, @PathVariable String versionname, @PathVariable String servername) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final boolean openServer = context.getPlugInUnitServers().openServer(servername);
        return new Result().setData(openServer).setFinish(true).setMessage("open service " + openServer + " " + pluginunitname + " " + servername);
    }

    /**
     * 开启对应插件的最新版本的服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/openlatest/{pluginunitname}")
    private Result openLatestServerByName(@PathVariable String pluginunitname) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        final boolean openAllServer = latest.getPlugInUnitServers().openAllServer();
        return new Result().setData(openAllServer).setFinish(true).setMessage("open latest service " + openAllServer + " " + pluginunitname);
    }

    /**
     * 开启全部插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/openlatestall")
    private Result openAllLatestServer() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(true);
        if (plugInUnitContextWithNodes == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        plugInUnitContextWithNodes.forEach(latest -> latest.getContext().getPlugInUnitServers().openAllServer());
        return new Result().setData(true).setFinish(true).setMessage("open all latest service");
    }


    /**
     * 关闭某一个版本插件的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/closeall/{pluginunitname}/{versionname}")
    private Result closeAllServerByName(@PathVariable String pluginunitname, @PathVariable String versionname) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        context.getPlugInUnitServers().closeAllServer();
        return new Result().setData(true).setFinish(true).setMessage("close service " + pluginunitname);
    }

    /**
     * 关闭对应插件的对应服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/close/{pluginunitname}/{versionname}/{servername}")
    private Result closeServerByNameWithVersion(@PathVariable String pluginunitname, @PathVariable String versionname, @PathVariable String servername) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final boolean closeServer = context.getPlugInUnitServers().closeServer(servername);
        return new Result().setData(closeServer).setFinish(true).setMessage("close service " + closeServer + " " + pluginunitname + " " + servername);
    }

    /**
     * 关闭最新插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/closelatest/{pluginunitname}")
    private Result closeLatestServerByName(@PathVariable String pluginunitname) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        latest.getPlugInUnitServers().closeAllServer();
        return new Result().setData(true).setFinish(true).setMessage("close latest service " + pluginunitname);
    }

    /**
     * 关闭全部插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/closelatestall")
    private Result closeAllLatestServer() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(true);
        if (plugInUnitContextWithNodes == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        plugInUnitContextWithNodes.forEach(latest -> latest.getContext().getPlugInUnitServers().closeAllServer());
        return new Result().setData(true).setFinish(true).setMessage("restart all latest service");
    }

    /**
     * 重启某一个版本插件的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/restartall/{pluginunitname}/{versionname}")
    private Result restartAllServerByName(@PathVariable String pluginunitname, @PathVariable String versionname) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        context.getPlugInUnitServers().restartAll();
        return new Result().setData(true).setFinish(true).setMessage("restart service " + pluginunitname);
    }

    /**
     * 重启对应插件的对应服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/restart/{pluginunitname}/{versionname}/{servername}")
    private Result restartServerByNameWithVersion(@PathVariable String pluginunitname, @PathVariable String versionname, @PathVariable String servername) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final boolean closeServer = context.getPlugInUnitServers().restart(servername);
        return new Result().setData(closeServer).setFinish(true).setMessage("restart service " + closeServer + " " + pluginunitname + " " + servername);
    }

    /**
     * 重启最新插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/restartlatest/{pluginunitname}")
    private Result restartLatestServerByName(@PathVariable String pluginunitname) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        latest.getPlugInUnitServers().restartAll();
        return new Result().setData(true).setFinish(true).setMessage("restart latest service " + pluginunitname);
    }

    /**
     * 重启全部插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/restartlatestall")
    private Result restartAllLatestServer() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(true);
        if (plugInUnitContextWithNodes == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        plugInUnitContextWithNodes.forEach(latest -> latest.getContext().getPlugInUnitServers().restartAll());
        return new Result().setData(true).setFinish(true).setMessage("restart all latest service");
    }

    /**
     * 刷新某一个版本插件的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/refreshall/{pluginunitname}/{versionname}")
    private Result refreshAllServerByName(@PathVariable String pluginunitname, @PathVariable String versionname) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        context.getPlugInUnitServers().refresh();
        return new Result().setData(true).setFinish(true).setMessage("restart service " + pluginunitname);
    }

    /**
     * 刷新最新插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/refreshlatest/{pluginunitname}")
    private Result refreshLatestServerByName(@PathVariable String pluginunitname) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        latest.getPlugInUnitServers().refresh();
        return new Result().setData(true).setFinish(true).setMessage("restart latest service " + pluginunitname);
    }

    /**
     * 刷新全部服务的最新版本的服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/refreshlatestall")
    private Result refreshAllLatestServer() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(true);
        if (plugInUnitContextWithNodes == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        plugInUnitContextWithNodes.forEach(a -> a.getContext().getPlugInUnitServers().refresh());
        return new Result().setData(true).setFinish(true).setMessage("restart all latest service");
    }

    /*
      ==============================================================================
     */

    /**
     * 开启某一个插件的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getstate/{pluginunitname}/{versionname}")
    private Result getAllServerStateByName(@PathVariable String pluginunitname, @PathVariable String versionname) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final HashMap<String, RunState> allServerState = context.getPlugInUnitServers().getAllServerState();
        return new Result().setData(allServerState).setFinish(true).setMessage("get service states " + pluginunitname);
    }

    /**
     * 得到对应版本插件的对应服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getstate/{pluginunitname}/{versionname}/{servername}")
    private Result getStateServerByNameWithVersion(@PathVariable String pluginunitname, @PathVariable String versionname, @PathVariable String servername) {
        final HashMapWithNodeImpl.PlugInUnitContextWithNode plugInUnitContextWithNode = registerCenterFactory.getByName(pluginunitname, versionname);
        if (plugInUnitContextWithNode == null) {
            return new Result().setData(false).setFinish(true).setMessage("No such version pluginunit");
        }
        final NewPlugInUnitWithContext context = plugInUnitContextWithNode.getContext();
        final RunState serverState = context.getPlugInUnitServers().getServerState(servername);
        return new Result().setData(serverState).setFinish(true).setMessage("get service state " + serverState + " " + pluginunitname + " " + servername);
    }

    /**
     * 得到对应插件的最新版本的服务状态
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getstatelatest/{pluginunitname}")
    private Result getStateLatestServerByName(@PathVariable String pluginunitname) {
        final NewPlugInUnitWithContext latest = registerCenterFactory.getLatest(pluginunitname);
        if (latest == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        final HashMap<String, RunState> allServerState = latest.getPlugInUnitServers().getAllServerState();
        return new Result().setData(allServerState).setFinish(true).setMessage("get latest service state" + pluginunitname);
    }

    /**
     * 开启全部插件的最新版本的全部服务
     *
     */
    @Contract(pure = true)
    @RequestMapping("/getstatelatestall")
    private Result getAllLatestServer() {
        final List<HashMapWithNodeImpl.PlugInUnitContextWithNode> plugInUnitContextWithNodes = registerCenterFactory.getAll(true);
        if (plugInUnitContextWithNodes == null) {
            return new Result().setData(false).setFinish(true).setMessage("no such latest plug in unit name!!");
        }
        HashMap<String, HashMap<String, RunState>> map = new HashMap<>(plugInUnitContextWithNodes.size());
        plugInUnitContextWithNodes.forEach(latest -> map.put(latest.getContext().getPlugInUnitName(), latest.getContext().getPlugInUnitServers().getAllServerState()));
        return new Result().setData(map).setFinish(true).setMessage("get all latest service state");
    }
}
