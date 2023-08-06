package com.qxc.pluginunit.construct;

import com.qxc.pluginunit.PlugInUnitServer;
import com.qxc.pluginunit.PlugInUnitServers;
import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.ThreadPoolManage;
import com.qxc.utiles.filesystemtools.FileUtiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/9 15:34
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@Slf4j
public class PlugInUnitServersImpl implements PlugInUnitServers {

    String serverParentPath;

    ArrayList<PlugInUnitServer> containsServer;

    transient ThreadPoolManage pool;

    int liveSize = 0;
    int allSize;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PlugInUnitServersImpl(String serverParentPath, ThreadPoolManage pool, boolean startAll) {
        this.pool = pool;
        this.serverParentPath = serverParentPath;
        allSize = loadServers();
        containsServer.sort((a, b) -> b.getRunPriority() - a.getRunPriority());
        if (startAll) {
            openAllServer();
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PlugInUnitServersImpl(String serverParentPath, ThreadPoolManage pool) {
        this(serverParentPath, pool, false);
    }

    /**
     * 导入当前目录中所有的服务
     *
     */
    @Override
    public int loadServers() {
        List<String> arr = FileUtiles.getChildrenDir(serverParentPath, path -> new File(path + File.separator + "config.json").exists());
        containsServer = new ArrayList<>();
        if (arr == null) {
            log.info("No server found");
            return 0;
        }
        for (String dir : arr) {
            final PlugInUnitServerImpl plugInUnitServer = new PlugInUnitServerImpl(dir + File.separator + "config.json", dir, this.pool);
            containsServer.add(plugInUnitServer);
        }
        return containsServer.size();
    }

    /**
     * 开启服务
     *
     */
    @Override
    public boolean openAllServer() {
        for (int i = 0; i < allSize; i++) {
            containsServer.get(i).open();
            log.info(containsServer.get(i).getServerName() + " start success!!");
            ++liveSize;
        }
        return true;
    }

    /**
     * 查询当前服务的状态
     *
     */
    @Override
    public RunState getServerState(String serveName) {
        final PlugInUnitServer ps = containServer(serveName);
        if (ps != null) {
            return ps.getCurrentState();
        }
        return null;
    }

    /**
     * 获取当前所有服务的状态
     *
     */
    @Override
    public HashMap<String, RunState> getAllServerState() {
        HashMap<String, RunState> ans = new HashMap<>(containsServer.size());
        for (PlugInUnitServer ps : containsServer) {
            ans.put(ps.getServerName(), ps.getCurrentState());
        }
        return ans;
    }

    /**
     * 打开目标服务
     *
     */
    @Override
    public boolean openServer(String serverName) {
        final PlugInUnitServer plug = containServer(serverName);
        if (plug != null) {
            return plug.open();
        }
        return false;
    }

    /**
     * 关闭目标服务
     *
     */
    @Override
    public boolean closeServer(String serverName) {
        final PlugInUnitServer plug = containServer(serverName);
        if (plug != null) {
            return plug.close();
        }
        return false;
    }

    /**
     * 重启某个服务
     *
     */
    @Override
    public boolean restart(String serverName) {
        final PlugInUnitServer plug = containServer(serverName);
        if (plug != null) {
            return plug.restart();
        }
        return false;
    }

    /**
     * 重启全部服务
     */
    @Override
    public void restartAll() {
        containsServer.forEach(PlugInUnitServer::restart);
    }

    /**
     * 关闭所有服务
     */
    @Override
    public void closeAllServer() {
        for (PlugInUnitServer ps : containsServer) {
            ps.close();
        }
    }

    /**
     * 刷新文件夹查找新服务
     */
    @Override
    public void refresh() {
        List<String> arr = FileUtiles.getChildrenDir(serverParentPath, path -> new File(path + File.separator + "config.json").exists());
        if (arr == null) {
            log.info("No server found");
            return;
        }
        // 刷新父文件夹中的服务
        List<PlugInUnitServer> arrList = new ArrayList<>();
        for (String dir : arr) {
            final PlugInUnitServerImpl plugInUnitServer = new PlugInUnitServerImpl(dir + File.separator + "config.json", dir, this.pool);
            arrList.add(plugInUnitServer);
        }
        arrList.sort((a, b) -> b.getRunPriority() - a.getRunPriority());
        // 移除arrList中没有的PlugInUnitServer
        containsServer.removeIf(plugInUnitServer -> {
            final boolean contains = arrList.contains(plugInUnitServer);
            if (!contains) {
                log.info(plugInUnitServer.close() + " remove old version server");
                allSize--;
            }
            return !contains;
        });
        // 向containsServer中添加新版本
        for (PlugInUnitServer ps : arrList) {
            if (!containsServer.contains(ps)) {
                containsServer.add(ps);
                allSize++;
                log.info(ps.open() + " start new version server");
            }
        }
    }

    /**
     * 查询是否存在对应的服务
     *
     */
    @Override
    public PlugInUnitServer containServer(String serverName) {
        for (PlugInUnitServer ps : containsServer) {
            if (ps.getServerName().equals(serverName)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * 获取所有服务
     *
     */
    @Override
    public PlugInUnitServer[] getAllServers() {
        return containsServer.toArray(new PlugInUnitServer[allSize]);
    }

    @Override
    public String toString() {
        return "PlugInUnitServersImpl{" +
                "serverParentPath='" + serverParentPath + '\'' +
                ", containsServer=" + containsServer +
                ", pool=" + pool +
                '}';
    }
}
