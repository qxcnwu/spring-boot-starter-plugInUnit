import com.qxc.configuration.SpringConfiguration;
import com.qxc.threadexector.dirctoryautolisten.DirectoryTargetMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 14:54
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class ListenFactoryTest {
    @Resource
    DirectoryTargetMonitor directoryTargetMonitor;

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() throws IOException {
        System.in.read();
    }
}
