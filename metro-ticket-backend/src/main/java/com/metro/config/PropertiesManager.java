package com.metro.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private static Properties properties;
    
    private PropertiesManager() {}

    public static synchronized Properties getInstance() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = PropertiesManager.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("Properties file not found");
                }
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load properties", e);
            }
        }
        return properties;
    }
}
