import com.qxc.threadexector.documentthreadexector.MethodWithOrder;
import com.qxc.utiles.reflectancetools.ClassUtiles;
import com.qxc.utiles.pluginunittools.CheckType;
import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static com.qxc.utiles.reflectancetools.MethodsUtiles.makeCheckMap;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 9:58
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
@Slf4j
public class ClassUtilTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() throws IOException, ClassNotFoundException {
        final ArrayList<Class<?>> allClassWithAnnotation = ClassUtiles.getAllClassWithAnnotation("com.qxc", CheckType.class);
        for (Class<?> clas : allClassWithAnnotation) {
            log.info(String.valueOf(clas));
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        final ArrayList<Class<?>> allClassWithAnnotation = ClassUtiles.getAllClassWithAnnotation("com.qxc", CheckType.class);
        final HashMap<CheckTypeEnum, PriorityQueue<MethodWithOrder>> checkTypeEnumPriorityQueueHashMap = makeCheckMap(allClassWithAnnotation);
        System.out.println(checkTypeEnumPriorityQueueHashMap);
    }
}
