import com.qxc.configuration.SpringConfiguration;
import com.qxc.pluginunit.construct.PlugInUnit;
import com.qxc.pluginunit.construct.PlugInUnitMaker;
import com.qxc.threadexector.checkthreadexector.CheckThreadExector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 20:29
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class CheckFactoryTest {
    @Resource
    private CheckThreadExector checkThreadExector;
    @Resource
    private PlugInUnitMaker plugInUnitMaker;

    /**
     * 测试提交方法
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        String parent="D:\\JAVA\\ChangeableFrameWork\\ToolSimple";
        final PlugInUnit plugInUnit = plugInUnitMaker.makePlugInUnit(parent);
        final Future<Boolean> submit = checkThreadExector.submit(plugInUnit);
        final Future<Boolean> submit2 = checkThreadExector.submit(plugInUnit);
        final Future<Boolean> submit3 = checkThreadExector.submit(plugInUnit);
        final Future<Boolean> submit4 = checkThreadExector.submit(plugInUnit);
        final Future<Boolean> submit5 = checkThreadExector.submit(plugInUnit);
        System.out.println(submit.get());
        System.out.println(submit2.get());
        System.out.println(submit3.get());
        System.out.println(submit4.get());
        System.out.println(submit5.get());

    }
}
