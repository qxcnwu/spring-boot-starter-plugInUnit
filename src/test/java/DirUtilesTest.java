import com.qxc.utiles.filesystemtools.DirUtiles;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 15:19
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class DirUtilesTest {
    @SuppressWarnings("MissingJavadoc")
    @Test
    public void TEST1(){
        ArrayList<Path> arr= new ArrayList<>();
        DirUtiles.getAllDir(new File("D:\\JAVA\\ChangeableFrameWork").toPath(),arr);
        for(Path p:arr){
            System.out.println(p);
        }
    }
}
