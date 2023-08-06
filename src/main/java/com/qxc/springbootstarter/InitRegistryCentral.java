package com.qxc.springbootstarter;

import com.qxc.configuration.BaseConfiguration;
import com.qxc.threadexector.eventbus.EventBus;
import com.qxc.threadexector.registrationcentercore.RegisterCenterFactory;
import com.qxc.utiles.reflectancetools.ClassUtiles;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

import static com.qxc.utiles.reflectancetools.ClassUtiles.getAllMethodWithAnnotation;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 22:25
 * @Version 1.0
 * @PACKAGE com.qxc.springbootstarter
 */
@Component
@Slf4j
public class InitRegistryCentral implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * SpringBoot容器初始化所有Bean后，对更目录下的容器进行加载
     *
     */
    private final RegisterCenterFactory registerCenterFactory;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public InitRegistryCentral(RegisterCenterFactory registerCenterFactory) {
        this.registerCenterFactory = registerCenterFactory;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
        final ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        BaseConfiguration baseConfiguration = applicationContext.getBean(BaseConfiguration.class);
        final EventBus eventBus = applicationContext.getBean(EventBus.class);
        try {
            final ArrayList<ClassUtiles.ClazzWithMethod> allMethodWithAnnotation = getAllMethodWithAnnotation(baseConfiguration.getBasePackage());
            for (ClassUtiles.ClazzWithMethod cm : allMethodWithAnnotation) {
                final Class<?> clazz = cm.getClazz();
                eventBus.register(applicationContext.getBean(clazz));
            }
            log.info("EVENTBUS registry success!!");
        } catch (ClassNotFoundException | IOException e) {
            log.info(e.getMessage());
        }
        registerCenterFactory.start();
    }
}
