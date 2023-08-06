package com.qxc.threadexector.registerthreadexector;

import com.qxc.threadexector.checkthreadexector.CheckThreadExector;
import com.qxc.threadexector.documentthreadexector.PlugInUnitDocumentFactory;
import com.qxc.threadexector.eventbus.EventBus;
import com.qxc.threadexector.serverthreadexector.PlugInUnitServerFactory;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 14:04
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.registerthreadexector
 */
@Component
@Data
public class RegisterThreadExectorContext {
    @Resource
    private final CheckThreadExector checkThreadExector;

    @Resource
    private final PlugInUnitDocumentFactory plugInUnitDocumentFactory;

    @Resource
    private final PlugInUnitServerFactory plugInUnitServerFactory;

    @Resource
    private final EventBus eventBus;
}
