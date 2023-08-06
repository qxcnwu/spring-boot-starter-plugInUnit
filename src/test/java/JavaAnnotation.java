import com.qxc.pluginunit.RunState;
import com.qxc.utiles.reflectancetools.MethodOrder;
import com.qxc.utiles.reflectancetools.CloneInstance;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 18:59
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
@Slf4j
public class JavaAnnotation implements CloneInstance<JavaAnnotation> {

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public JavaAnnotation() {

    }

    @Override
    public JavaAnnotation clone() {
        try {
            return (JavaAnnotation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method2", "method3"})
    public RunState method1() {
        System.out.println("method 1 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method3", "method4", "method5"})
    public RunState method2() {
        System.out.println("method 2 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method6", "method5"})
    public RunState method3() {
        System.out.println("method 3 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method3", "method5"})
    public RunState method4() {
        System.out.println("method 4 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder()
    public RunState method5() {
        System.out.println("method 5 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method5"})
    public RunState method6() {
        System.out.println("method 6 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method8"})
    public RunState method7() {
        System.out.println("method 7 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method9"})
    public RunState method8() {
        System.out.println("method 8 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder()
    public RunState method9() {
        System.out.println("method 9 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder()
    public RunState method10(ArrayList<String> arr) {
        log.debug("======================add====================");
        log.debug(String.valueOf(arr));
        log.debug("====================add done====================");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method10"})
    public RunState method11() {
        System.out.println("method 11 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method10"})
    public RunState method12() {
        System.out.println("method 12 exec");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method11"})
    public RunState method16() {
        System.out.println("method 16 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method12"})
    public RunState method13() {
        System.out.println("method 13 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method11"})
    public RunState method14() {
        System.out.println("method 14 exec");
        return RunState.DONE;
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder(methods = {"method12"})
    public RunState method15() {
        System.out.println("method 15 exec");
        return RunState.DONE;
    }
}
