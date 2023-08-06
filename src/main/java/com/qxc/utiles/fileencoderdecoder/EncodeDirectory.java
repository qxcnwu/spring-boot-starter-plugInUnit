package com.qxc.utiles.fileencoderdecoder;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @Author qxc
 * @Date 2023 2023/7/23 20:25
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.fileencoderdecoder
 */
public final class EncodeDirectory {
    final static int ISFILE = 1;
    final static int CENT = 1;
    final static int CENTSON = 1;
    final static int NAME = 16;
    final static int CEN = 32;
    final static int ALL = ISFILE + CENT + CENTSON + NAME + CEN;
    static int NOW = 0;

    public static @NotNull ArrayList<byte[]> encodeDirectory(String path, int n, boolean isFirst) {
        if (isFirst) {
            NOW = 0;
        } else {
            NOW++;
        }
        ArrayList<byte[]> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        byte[] tmp = new byte[ALL];
        tmp[0] = 1;
        // 父文件夹序号
        tmp[ISFILE] = (byte) n;
        // 当前文件夹序号
        tmp[ISFILE + CENT] = (byte) NOW;
        // 取出文件夹编码给当前文件夹下的其他文件掩膜
        byte[] tp = new byte[CENT + CENTSON];
        System.arraycopy(tmp, ISFILE, tp, 0, CENT + CENTSON);
        File file = new File(path);
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) {
                ans.addAll(encodeDirectory(f.getPath(), NOW, false));
            } else {
                final ByteWithSha fileByte = getFileByte(f.getPath(), tp);
                ans.add(fileByte.ans);
                // 对该文件夹下的所有文件的sha256值进行相加后，采用sha256计算散列值，作为当前文件夹文件的sha256
                sb.append(fileByte.sha256);
            }
        }
        // 文件夹名称
        final byte[] md5 = FileEncoderUtiles.getMd5(file.getName());
        System.arraycopy(md5, 0, tmp, ISFILE + CENT + CENTSON, NAME);
        // 文件夹内容的sha256
        final byte[] sha256 = FileEncoderUtiles.getSHA256(sb.toString());
        System.arraycopy(sha256, 0, tmp, ISFILE + CENT + NAME + CENTSON, CEN);
        ans.add(tmp);
        return ans;
    }

    /**
     * 获取对应文件的byte数组
     *
     * @return
     */
    @Contract(pure = true)
    private static @NotNull ByteWithSha getFileByte(String path, byte[] ns) {
        byte[] ans = new byte[ALL];
        // 不是文件夹标记为0
        ans[0] = 0;
        // 文件夹层数
        System.arraycopy(ns, 0, ans, ISFILE, CENT + CENTSON);
        // 获取名称的md5
        File file = new File(path);
        final byte[] md5 = FileEncoderUtiles.getMd5(file.getName());
        System.arraycopy(md5, 0, ans, ISFILE + CENT + CENTSON, NAME);
        // 获取
        final byte[] sha256 = FileEncoderUtiles.getSHA256(file);
        System.arraycopy(sha256, 0, ans, ISFILE + CENT + NAME + CENTSON, CEN);
        return new ByteWithSha(ans, FileEncoderUtiles.toHexString(sha256));
    }

    @AllArgsConstructor
    static class ByteWithSha {
        byte[] ans;
        String sha256;
    }

}
