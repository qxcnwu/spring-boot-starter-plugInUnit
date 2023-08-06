import com.qxc.utiles.reflectancetools.PipeLine;
import com.qxc.utiles.containertools.ThreadSafeSetExector;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/11 20:37
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
@Slf4j
public class ThreadSafeSetExectorTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() {
        PipeLine<JavaAnnotation> pipeLine = new PipeLine<>(new JavaAnnotation());
        ThreadSafeSetExector<String, JavaAnnotation> t = new ThreadSafeSetExector<>(pipeLine);
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test2() throws IOException {
        PipeLine<JavaAnnotation> pipeLine = new PipeLine<>(new JavaAnnotation());
        ThreadSafeSetExector<String, JavaAnnotation> t = new ThreadSafeSetExector<>(10, 1000, pipeLine);
        t.start();
        new Thread(t::listen).start();

        ArrayList<String> ans = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            ans.add(String.valueOf(i));
            int finalI = i;
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(finalI + 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                t.add(ans.get(finalI));
            }, i + "Th").start();
        }

        // 测试删除元素
        try {
            TimeUnit.SECONDS.sleep(30);
            t.remove(ans.get(25));
            System.out.println("==============remove==============");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 测试清除元素
        try {
            TimeUnit.SECONDS.sleep(30);
            t.clear();
            System.out.println("==============clear==============");
            TimeUnit.SECONDS.sleep(10);
            t.refresh();
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 尝试关闭线程池
        t.shutdone();
        System.in.read();
    }
}
