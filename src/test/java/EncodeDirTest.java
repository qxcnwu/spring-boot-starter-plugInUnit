import com.qxc.utiles.fileencoderdecoder.DecodeArrayByte;
import com.qxc.utiles.fileencoderdecoder.EncodeDirectory;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author qxc
 * @Date 2023 2023/7/23 21:10
 * @Version 1.0
 * @PACKAGE PACKAGE_NAME
 */
public class EncodeDirTest {
    @Test
    public void test1() {
        final ArrayList<byte[]> bytes = EncodeDirectory.encodeDirectory("F:\\dists", 0, true);
        for (int i = 0; i < bytes.size(); i++) {
            System.out.println(Arrays.toString(bytes.get(i)));
        }
        final ArrayList<DecodeArrayByte.Directory> directories = DecodeArrayByte.decodeDirectory(bytes);
        final ArrayList<DecodeArrayByte.File> files = DecodeArrayByte.decodeFile(bytes);
    }

    @Test
    public void test2() {
        String a = "a";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            sb.append(a);
        }
        final byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        System.out.println(sb.toString());
        System.out.println(bytes.length);
    }
}