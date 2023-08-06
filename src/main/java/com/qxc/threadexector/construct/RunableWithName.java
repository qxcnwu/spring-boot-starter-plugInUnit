package com.qxc.threadexector.construct;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author qxc
 * @Date 2023 2023/7/7 15:55
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.construct
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class RunableWithName {
    Runnable run;
    String name;
}