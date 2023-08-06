import com.qxc.pluginunit.construct.PlugInUnitServersImpl;
import com.qxc.pluginunit.PlugInUnitServers;
import com.qxc.pluginunit.RunState;
import com.qxc.threadexector.construct.BasicThreadPool;
import com.qxc.threadexector.ThreadPoolManage;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/9 19:12
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class PlugInUnitServersTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() throws InterruptedException {
        String path = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server";
        ThreadPoolManage pool = new BasicThreadPool("StreamThreadPool", 2, 6, 4, 1000);
        PlugInUnitServers ps = new PlugInUnitServersImpl(path, pool);
        ps.openAllServer();
        TimeUnit.SECONDS.sleep(10);
        final HashMap<String, RunState> allServerState = ps.getAllServerState();
        System.out.println(allServerState);

        TimeUnit.SECONDS.sleep(10);
        ps.closeAllServer();
        System.out.println(ps.getAllServerState());

        for (String name : allServerState.keySet()) {
            System.out.println(ps.openServer(name) + " open " + name);
        }
        TimeUnit.SECONDS.sleep(10);
        System.out.println(ps.getAllServerState());

        for (String name : allServerState.keySet()) {
            System.out.println(ps.closeServer(name) + " close " + name);
        }
        TimeUnit.SECONDS.sleep(10);
        System.out.println(ps.getAllServerState());

        ps.openAllServer();
        System.out.println("==================================");

        TimeUnit.SECONDS.sleep(20);
        System.out.println(ps.getAllServerState());

        System.out.println("================start refresh===========");
        ps.refresh();
        TimeUnit.SECONDS.sleep(20);
        System.out.println(ps.getAllServerState());

        ps.closeAllServer();
        System.out.println(ps.getAllServerState());
    }
}
