import com.qxc.utiles.reflectancetools.AnnotationTools;
import com.qxc.utiles.reflectancetools.PipeLine;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 14:52
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class AnnotationToolsTest {
    public static final String[] PARENTS = {};

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() {
        HashMap<String, PipeLine.MethodNode> map = new HashMap<>();
        map.put("1", new PipeLine.MethodNode().setName("1").setParents(new String[]{"3", "2"}).setId(1).setPnum(2));
        map.put("2", new PipeLine.MethodNode().setName("2").setParents(new String[]{"3", "4", "5"}).setId(2).setPnum(3));
        map.put("3", new PipeLine.MethodNode().setName("3").setParents(new String[]{"6", "5"}).setId(3).setPnum(2));
        map.put("4", new PipeLine.MethodNode().setName("4").setParents(new String[]{"5"}).setId(4).setPnum(1));
        map.put("5", new PipeLine.MethodNode().setName("5").setParents(PARENTS).setId(5).setPnum(0));
        map.put("6", new PipeLine.MethodNode().setName("6").setParents(new String[]{"5"}).setId(6).setPnum(1));
        map.put("7", new PipeLine.MethodNode().setName("7").setParents(new String[]{"8"}).setId(7).setPnum(1));
        map.put("8", new PipeLine.MethodNode().setName("8").setParents(new String[]{"9"}).setId(8).setPnum(1));
        map.put("9", new PipeLine.MethodNode().setName("9").setParents(PARENTS).setId(9).setPnum(0));
        System.out.println(AnnotationTools.checkCircleReference(map));
        final List<PipeLine.MethodNode> parse = AnnotationTools.parse(map);
        for (PipeLine.MethodNode m : parse) {
            System.out.println(m);
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test2() throws ExecutionException, InterruptedException {
        JavaAnnotation javaAnnotation = new JavaAnnotation();
        PipeLine<JavaAnnotation> pipeLine = new PipeLine<>(javaAnnotation, true);
        System.out.println(pipeLine);
        pipeLine.exec("qxc");
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        JavaAnnotation javaAnnotation = new JavaAnnotation();
        PipeLine<JavaAnnotation> pipeLine = new PipeLine<>(javaAnnotation, false);
        System.out.println(pipeLine);
        pipeLine.exec("qxcqxcqxcqxcqxcqxc");
    }
}

