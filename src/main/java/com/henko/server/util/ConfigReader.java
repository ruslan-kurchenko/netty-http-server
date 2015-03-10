package com.henko.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private final String path;

    public ConfigReader(String path) {
        this.path = path;
    }

    public Properties getConfigs() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));

        return prop;
    }
}
