package org.rushland.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.util.UUID;

/**
 * Managed by romain on 08/11/2014.
 */
public class FileUtils {
    private final static String INST_MAP_PATH = "plugins/rushland/maps/instances/";

    @SneakyThrows
    public static String copy(String path) {
        String newPath = INST_MAP_PATH + UUID.randomUUID().toString();
        org.apache.commons.io.FileUtils.copyDirectory(new File(path), new File(newPath));

        return newPath;
    }

    @SneakyThrows
    public static void remove(String path) {
        org.apache.commons.io.FileUtils.deleteDirectory(new File(path));
    }
}
