package com.qxc.utiles.filesystemtools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 15:11
 * @Version 1.0
 * @PACKAGE com.qxc.utiles.filesystemtools
 */
public final class DirUtiles {
    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static void getAllDir(Path path, @NotNull List<Path> ans) {
        ans.add(path);
        final File[] files = new File(path.toString()).listFiles();
        assert files != null;
        for (File file : files) {
            if(file.isDirectory()){
                getAllDir(file.toPath(),ans);
            }
        }
    }


}
