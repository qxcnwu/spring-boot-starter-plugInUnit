package com.qxc.threadexector.dirctoryautolisten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 14:35
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.dirctoryautolisten
 */
@Data
@Slf4j
@AllArgsConstructor
public class FileChangeEvent {
    private final Path path;
    private final WatchEvent.Kind<?> kind;
}
