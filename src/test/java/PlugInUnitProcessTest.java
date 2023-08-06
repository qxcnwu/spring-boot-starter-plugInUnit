import com.qxc.pluginunit.construct.PlugInUnitProcessImpl;
import com.qxc.pluginunit.PlugInUnitProcess;
import com.qxc.threadexector.construct.BasicThreadPool;
import com.qxc.threadexector.ThreadPoolManage;
import com.qxc.utiles.filesystemtools.FileUtiles;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 10:53
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class PlugInUnitProcessTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testP() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "main.bat";
        String execDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2";
        String saveDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\output.txt";
        String errDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\err.txt";
        processBuilder.command("python", "main.py").directory(new File(execDir));
        final Process process = Runtime.getRuntime().exec("python D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\main.py");
        System.out.println(process);

        Thread t = new Thread(() -> FileUtiles.writeByteToFile(saveDir, process.getErrorStream()));
        t.start();

        Thread t2 = new Thread(() -> FileUtiles.writeByteToFile(saveDir, process.getInputStream()));
        t2.start();

        TimeUnit.SECONDS.sleep(5);
        System.out.println("中断");
        process.destroy();
        System.out.println(process);
        System.out.println("end");
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testRun() throws InterruptedException, IOException {
        String command = "python D:\\ref\\models\\ParseText.py";
        String execDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2";
        String saveDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\output.txt";
        String errDir = "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\err.txt";
        ThreadPoolManage pool = new BasicThreadPool("StreamThreadPool", 2, 6, 4, 1000);
        PlugInUnitProcess p = new PlugInUnitProcessImpl("pythonThread", command, execDir, pool);
        System.out.println(p.run());
        System.out.println(p.getState());
        Thread.sleep(5000);
        System.out.println(p.getState());
        p.stop();
        TimeUnit.SECONDS.sleep(10);
        System.out.println("STOP");
        System.in.read();
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void TestRun() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server3\\")).command("start","main.cmd");
        processBuilder.start();
    }


}
