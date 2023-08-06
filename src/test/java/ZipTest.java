import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 21:31
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class ZipTest {
    @SuppressWarnings("MissingJavadoc")
    public static void extractZip(String filePath) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, new Adler32());
        ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
        ZipEntry zipEntry;

        FileOutputStream fileOutputStream;
        File savePath = new File(filePath.replace(".zip", ""));
        if (!savePath.exists()) {
            savePath.mkdir();
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            fileOutputStream = new FileOutputStream(savePath.getName() + "/" + zipEntry.getName());
            int x;
            byte[] bytes = new byte[1024];
            while ((x = bufferedInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes);
            }
            fileOutputStream.close();
        }
        zipInputStream.close();
        fileInputStream.close();
    }

    @SuppressWarnings("MissingJavadoc")
    @Test
    public void test() throws Exception {
        extractZip("D:\\JAVA\\ChangeableFrameWork\\ListenDir\\func1-v1.1.zip");
    }
}
