package com.qxc.threadexector.documentthreadexector;

import com.qxc.pluginunit.construct.PlugInUnit;
import com.qxc.pluginunit.construct.PlugInUnitDocumentImpl;
import com.qxc.pluginunit.PlugInUnitDocument;
import com.qxc.utiles.containertools.ThreadSafeSetExector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 15:58
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.documentthreadexector
 */
@Component
public class PlugInUnitDocumentFactory {
    private final ThreadSafeSetExector<String, DocumentPipeLine> threadSafeSetExector;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public PlugInUnitDocumentFactory(@NotNull DocumentThreadExectorFactory documentThreadExectorFactory) {
        threadSafeSetExector = documentThreadExectorFactory.getThreadSafeSetExector();
    }

    @SuppressWarnings("MissingJavadoc")
    public PlugInUnitDocument getPlugInUnitDocument(@NotNull PlugInUnit plugInUnit) {
        return new PlugInUnitDocumentImpl(plugInUnit.getDocMDName(), threadSafeSetExector);
    }
}
