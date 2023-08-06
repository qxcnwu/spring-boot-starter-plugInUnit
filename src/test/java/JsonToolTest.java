import com.qxc.pluginunit.construct.PlugInUnitServerImpl;
import org.junit.Test;

import java.io.IOException;

import static com.qxc.utiles.filesystemtools.JsonUtiles.parseJsonFile;
import static com.qxc.utiles.filesystemtools.JsonUtiles.saveToJson;

/**
 * @Author qxc
 * @Date 2023 2023/7/8 20:36
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class JsonToolTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testWrite() {
        PlugInUnitServerImpl.PlugInUnitServerConfig plugInUnitServerConfig = new PlugInUnitServerImpl.PlugInUnitServerConfig(
                "TestServer", "python D:\\ref\\models\\ParseText.py", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2", 5
        );
        saveToJson("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\config.json", plugInUnitServerConfig);
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testRead() throws IOException {
        final Object o = parseJsonFile("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\config.json", PlugInUnitServerImpl.PlugInUnitServerConfig.class);
        PlugInUnitServerImpl.PlugInUnitServerConfig obj = (PlugInUnitServerImpl.PlugInUnitServerConfig) o;
        System.out.println(obj);
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void testWrite1() {
        PlugInUnitServerImpl.PlugInUnitServerConfig plugInUnitServerConfig = new PlugInUnitServerImpl.PlugInUnitServerConfig(
                "TestServer2", "python D:\\ref\\models\\ParseText.py", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2", 5
        );
        saveToJson("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server2\\config.json", plugInUnitServerConfig);

        PlugInUnitServerImpl.PlugInUnitServerConfig plugInUnitServerConfig2 = new PlugInUnitServerImpl.PlugInUnitServerConfig(
                "TestServer3", "$dir/main.cmd", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server3", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server3", 10
        );
        saveToJson("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server3\\config.json", plugInUnitServerConfig2);

        PlugInUnitServerImpl.PlugInUnitServerConfig plugInUnitServerConfig3 = new PlugInUnitServerImpl.PlugInUnitServerConfig(
                "TestServer1", "java -jar $dir/GACSpBoot-0.0.1-SNAPSHOT.jar", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server1", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server1", 10
        );
        saveToJson("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server1\\config.json", plugInUnitServerConfig3);

        PlugInUnitServerImpl.PlugInUnitServerConfig plugInUnitServerConfig4 = new PlugInUnitServerImpl.PlugInUnitServerConfig(
                "TestServer4", "java -jar $dir/GACSpBoot-0.0.1-SNAPSHOT.jar", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server4", "D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server4", 12
        );
        saveToJson("D:\\JAVA\\ChangeableFrameWork\\ToolSimple\\server\\server4\\config.json", plugInUnitServerConfig4);
    }
}
