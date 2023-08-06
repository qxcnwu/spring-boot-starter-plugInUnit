import org.junit.Test;

import java.nio.file.StandardWatchEventKinds;
import java.util.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 16:25
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class JavaBasicTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a));
        queue.add(4);
        queue.add(41);
        queue.add(14);
        queue.add(423);
        queue.add(-4);
        queue.add(1);
        ArrayList<Integer> arr = new ArrayList<>(queue);
        System.out.println(arr);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test1(){
        System.out.println(StandardWatchEventKinds.ENTRY_MODIFY.name());
        System.out.println(StandardWatchEventKinds.OVERFLOW.name());
        System.out.println(StandardWatchEventKinds.ENTRY_DELETE.name());
        System.out.println(StandardWatchEventKinds.ENTRY_CREATE.name());
    }
}
