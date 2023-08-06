package com.qxc.threadexector.documentthreadexector;

import com.qxc.pluginunit.RunState;
import com.qxc.utiles.filesystemtools.FileUtiles;
import com.qxc.utiles.filesystemtools.PathParseUtiles;
import com.qxc.utiles.reflectancetools.MethodOrder;
import com.qxc.utiles.reflectancetools.CloneInstance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 20:56
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.documentthreadexector
 */
@Data
@Slf4j
public class DocumentPipeLine implements CloneInstance<DocumentPipeLine> {
    String RSTPath;
    String RSTCommand;
    String execDir;
    String newDir;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public DocumentPipeLine(String RSTPath, String RSTCommand, String execDir) {
        this.RSTPath = RSTPath;
        this.RSTCommand = RSTCommand;
        this.execDir = execDir;
        // 新产生的目录为这个
        newDir = Paths.get(new File(RSTPath).getParent(), "include").toString();
    }

    /**
     * 将新增加的Markdown文件写入核心配置
     *
     * @return
     */
    @MethodOrder(methods = {"removeMD"})
    public RunState writeToFile() {
        try {
            ArrayList<String> arr = FileUtiles.readRst(RSTPath);
            File files = new File(newDir);
            for (File file : Objects.requireNonNull(files.listFiles())) {
                final String name = file.getName();
                if (arr.contains("   include/" + name)) {
                    continue;
                }
                arr.add(12, "   include/" + name);
            }
            return FileUtiles.writeRst(arr, RSTPath) ? RunState.DONE : RunState.ERROR;
        } catch (IOException e) {
            log.error(e.getMessage());
            return RunState.ERROR;
        }
    }

    @SuppressWarnings("MissingJavadoc")
    @MethodOrder()
    public RunState removeMD(@NotNull ArrayList<String> newMDs) {
        // 需要将对应的MD文件进行拷贝
        for (String md : newMDs) {
            // 解析版本和名称
            String vN = PathParseUtiles.getVersionName(new File(md).getParentFile().getParent());
            String pN = PathParseUtiles.getPlugInUnitName(new File(md).getParentFile().getParent());
            FileUtiles.copyFile(md, Paths.get(newDir, pN + "_" + vN + "_" + new File(md).getName()).toString());
        }
        return RunState.DONE;
    }

    @Override
    public DocumentPipeLine clone() {
        return new DocumentPipeLine(RSTPath, RSTCommand, execDir);
    }
}