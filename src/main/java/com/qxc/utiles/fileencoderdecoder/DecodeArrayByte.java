package com.qxc.utiles.fileencoderdecoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @Author qxc
 * @Date 2023 2023/7/23 21:32
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.fileencoderdecoder
 */
@Slf4j
public final class DecodeArrayByte {
    final static int ISFILE = EncodeDirectory.ISFILE;
    final static int CENT = EncodeDirectory.CENT;
    final static int CENTSON = EncodeDirectory.CENTSON;
    final static int NAME = EncodeDirectory.NAME;
    final static int CEN = EncodeDirectory.CEN;

    @Contract(pure = true)
    public static @NotNull ArrayList<File> decodeFile(@NotNull ArrayList<byte[]> arr) {
        ArrayList<File> ans = new ArrayList<>();
        for (byte[] by : arr) {
            if (by[0] == 1) {
                continue;
            }
            ans.add(new File(getN(by), getNson(by), getName(by), getSha(by)));
        }
        return ans;
    }

    @Contract(pure = true)
    public static @NotNull ArrayList<Directory> decodeDirectory(@NotNull ArrayList<byte[]> arr) {
        ArrayList<Directory> ans = new ArrayList<>();
        for (byte[] by : arr) {
            if (by[0] == 0) {
                continue;
            }
            ans.add(new Directory(getN(by), getNson(by), getName(by), getSha(by)));
        }
        return ans;
    }

    @Contract(pure = true)
    private static int getN(byte @NotNull [] bytes) {
        return bytes[ISFILE];
    }

    @Contract(pure = true)
    private static int getNson(byte @NotNull [] bytes) {
        return bytes[ISFILE + CENT];
    }

    @Contract(pure = true)
    private static @NotNull String getName(byte[] bytes) {
        byte[] name = new byte[NAME];
        System.arraycopy(bytes, ISFILE + CENT, name, 0, NAME);
        return FileEncoderUtiles.toHexString(name);
    }

    @Contract(pure = true)
    private static @NotNull String getSha(byte[] bytes) {
        byte[] name = new byte[CEN];
        System.arraycopy(bytes, ISFILE + CENT + CENTSON + NAME, name, 0, CEN);
        return FileEncoderUtiles.toHexString(name);
    }

    @Data
    @AllArgsConstructor
    public static class File {
        Integer censon;
        Integer cen;
        String name;
        String sha;

        @Contract(pure = true)
        public File(int cen, int censon, String name, String sha) {
            this.cen = cen;
            this.censon = censon;
            this.name = name;
            this.sha = sha;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Directory {
        Integer cen;
        Integer censon;
        String name;
        String sha;
        ArrayList<File> files;

        @Contract(pure = true)
        public Directory(int cen, int censon, String name, String sha) {
            this.cen = cen;
            this.censon = censon;
            this.name = name;
            this.sha = sha;
        }
    }

}