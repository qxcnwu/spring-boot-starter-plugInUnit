package com.qxc.utiles;

import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * @Author qxc
 * @Date 2023 2023/7/5 16:19
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 * @Message Get current system information with cpu mem disk current_dir or others
 */
@Component
@Data
public final class CurrentSystemInfo {
    private final SystemInfo SYSTEM_INFO = new SystemInfo();
    private final HardwareAbstractionLayer HARDWARE = SYSTEM_INFO.getHardware();
    private final int CPU_Processors = Runtime.getRuntime().availableProcessors();
    private final double ALL_MEM = HARDWARE.getMemory().getTotal() / 1024.0 / 1024.0;
    private final String SYSTEMNAME = SYSTEM_INFO.getOperatingSystem().getFamily();

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "CurrentSystemInfo{" +
                "SYSTEM_INFO=" + SYSTEM_INFO +
                ", HARDWARE=" + HARDWARE +
                ", CPU_Processors=" + CPU_Processors +
                ", ALL_MEM=" + ALL_MEM +
                ", SYSTEMNAME='" + SYSTEMNAME + '\'' +
                '}';
    }

    private static class Holder {
        /**
         * 通过单例模式创建当前的系统对象
         */
        private static final CurrentSystemInfo INSTANCE = new CurrentSystemInfo();
    }

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Bean
    @Primary
    public static CurrentSystemInfo getInstance() {
        return Holder.INSTANCE;
    }
}

