package com.qxc.pluginunit.construct;

import com.qxc.utiles.pluginunittools.CheckTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @Author qxc
 * @Date 2023 2023/7/5 23:42
 * @Version 1.0
 * @PACKAGE com.qxc.pluginunit.construct
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PlugInUnit implements Serializable {
    private String parentPathName;
    private String docPathName;
    private String serverPathName;
    private String staticPathName;
    private String configPathName;
    private String srcPathName;
    private String docMDName;

    @SuppressWarnings("MissingJavadoc")
    public String getByCheckType(@NotNull CheckTypeEnum checkTypeEnum) {
        switch (checkTypeEnum) {
            case SERVERDIR:
                return serverPathName;
            case PARENTDIR:
                return parentPathName;
            case DOCDIR:
                return docPathName;
            case STATICDIR:
                return staticPathName;
            case CONFIG:
                return configPathName;
            case SRCDIR:
                return srcPathName;
            default:
                return "";
        }
    }
}
