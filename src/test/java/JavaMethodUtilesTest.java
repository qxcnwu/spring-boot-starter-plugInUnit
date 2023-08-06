import com.qxc.utiles.reflectancetools.MethodsUtiles;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import org.junit.Test;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 14:28
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class JavaMethodUtilesTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() {
        final int i = MethodsUtiles.makeCode(0, new CheckTypeEnum[]{CheckTypeEnum.SRCDIR, CheckTypeEnum.SERVERDIR}, new CheckTypeEnum[]{CheckTypeEnum.CONFIG, CheckTypeEnum.PARENTDIR});
        System.out.println(Integer.toBinaryString(i));
    }
}
