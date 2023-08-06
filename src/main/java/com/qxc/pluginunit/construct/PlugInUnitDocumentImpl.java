package com.qxc.pluginunit.construct;

import com.qxc.pluginunit.PlugInUnitDocument;
import com.qxc.threadexector.documentthreadexector.DocumentPipeLine;
import com.qxc.utiles.containertools.ThreadSafeSetExector;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

/**
 * @Author qxc
 * @Date 2023 2023/7/11 18:55
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@Data
@Slf4j
public class PlugInUnitDocumentImpl implements PlugInUnitDocument {
    private String documentPath;
    private ThreadSafeSetExector<String, DocumentPipeLine> threadSafeSetExector;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PlugInUnitDocumentImpl(String documentPath, ThreadSafeSetExector<String, DocumentPipeLine> threadSafeSetExector) {
        this.documentPath = documentPath;
        this.threadSafeSetExector = threadSafeSetExector;
    }

    /**
     * 更新markdown文件
     *
     */
    @Override
    public void updateMarkdown() {
        this.threadSafeSetExector.add(documentPath);
    }

    @Override
    public String toString() {
        return "PlugInUnitDocumentImpl{" +
                "documentPath='" + documentPath + '\'' +
                '}';
    }
}
