package com.qxc.databasecentral.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 19:56
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.pojo
 */
@Data
@TableName("PlugInUnitPojo")
@AllArgsConstructor
public class PlugInUnitPojo {
    int id;
    String pluginunit;
    String version;
    String rootPath;
    boolean islatest;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public PlugInUnitPojo(String plugInUnit, String version, String rootPath) {
        this.pluginunit = plugInUnit;
        this.version = version;
        this.rootPath = rootPath;
    }
}
