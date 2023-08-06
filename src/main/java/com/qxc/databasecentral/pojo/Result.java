package com.qxc.databasecentral.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;

/**
 * @Author qxc
 * @Date 2023 2023/7/16 17:16
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.pojo
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Object data;
    private boolean finish;
    private String message;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public Result(String message) {
        this.message = message;
        this.finish = false;
    }
}
