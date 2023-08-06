package com.qxc.threadexector.dirctoryautolisten;

import com.qxc.threadexector.configuration.DirectoryTargetMonitorConfiguration;
import com.qxc.threadexector.eventbus.EventBus;
import com.qxc.threadexector.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.qxc.utiles.filesystemtools.ZipUtils.zipFileCheckAndUnzip;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 15:50
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.dirctoryautolisten
 */
@Component
@Slf4j
public class PlugInUnitZipExectorFactory {

    private final DirectoryTargetMonitorConfiguration configuration;
    private final EventBus eventBus;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public PlugInUnitZipExectorFactory(DirectoryTargetMonitorConfiguration configuration, EventBus eventBus) {
        this.configuration = configuration;
        this.eventBus = eventBus;
    }

    @SuppressWarnings("MissingJavadoc")
    @Subscribe(topic = {"ENTRY_CREATE", "ENTRY_MODIFY"})
    public void zipCreate(String path) {
        try {
            final String rootPlugInUnitPath = zipFileCheckAndUnzip(path, configuration.getBaseDir());
            // 这里需要添加组件
            log.info(rootPlugInUnitPath + " has been be create!!");
            // 提交解压完成的事件
            eventBus.post(rootPlugInUnitPath,"pluginunit unzip");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Warning base root");
        }
    }
}
