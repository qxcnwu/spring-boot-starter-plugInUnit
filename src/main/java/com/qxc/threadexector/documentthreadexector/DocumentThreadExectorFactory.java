package com.qxc.threadexector.documentthreadexector;

import com.qxc.threadexector.configuration.DocumentThreadExectorConfiguration;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.reflectancetools.PipeLine;
import com.qxc.utiles.containertools.ThreadSafeSetExector;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 20:39
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.documentthreadexector
 */
@Component
@Data
public class DocumentThreadExectorFactory {
    private final DocumentThreadExectorConfiguration configuration;
    private final PipeLine<DocumentPipeLine> pipeLine;
    private final ThreadSafeSetExector<String, DocumentPipeLine> threadSafeSetExector;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public DocumentThreadExectorFactory(@NotNull DocumentThreadExectorConfiguration configuration) {
        this.configuration = configuration;
        pipeLine = new PipeLine<>(new DocumentPipeLine(configuration.getRSTPath(), configuration.getRSTCommand(), configuration.getExecDir()), configuration.isMultiThread(), configuration.getMultiThreadNumber());
        threadSafeSetExector = new ThreadSafeSetExector<>(configuration.getExectorLimitTime(), configuration.getExectorLimitNumber(), pipeLine);
        run();
    }

    private void run() {
        threadSafeSetExector.start();
    }
}
