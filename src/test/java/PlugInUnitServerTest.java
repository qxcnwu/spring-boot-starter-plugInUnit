import com.qxc.pluginunit.construct.PlugInUnitServerImpl;
import com.qxc.pluginunit.PlugInUnitServer;
import com.qxc.threadexector.construct.BasicThreadPool;
import com.qxc.threadexector.construct.LinkedRunnableQueue;
import com.qxc.threadexector.DenyPolice;
import com.qxc.threadexector.RunnableQueue;
import com.qxc.threadexector.ThreadPoolManage;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/8 20:57
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class PlugInUnitServerTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testRun() throws InterruptedException, IOException {
        final String ConfigPath = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\config.json";
        ThreadPoolManage pool = new BasicThreadPool("StreamThreadPool", 2, 6, 4, 1000);
        PlugInUnitServer plug = new PlugInUnitServerImpl(ConfigPath,"D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\", pool);

        plug.open();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(plug.getCurrentState());

        System.out.println("=================================");
        plug.restart();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(plug.getCurrentState());

        System.out.println("=================================");
        plug.close();
        System.out.println(plug.getCurrentState());
        System.in.read();
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testThread() {
        ThreadPoolManage pool = new BasicThreadPool("StreamThreadPool", 2, 6, 4, 1000);
        final RunnableQueue queue = new LinkedRunnableQueue(1000, new DenyPolice.DiscardDenyPolice(), pool);

        Thread th = new Thread(() -> {
            try {
                queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread th2 = new Thread(() -> {
            try {
                queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        th.start();
        th2.start();

        System.out.println("qxc");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
