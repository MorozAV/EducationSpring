package com.alexfess.educationspring;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final String PASSWORD_KEY = "db.password";
    private static String dbPassword;

    static {
        Properties properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
            dbPassword = properties.getProperty(PASSWORD_KEY);

            if (dbPassword == null || dbPassword.trim().isEmpty()) {
                throw new RuntimeException("Database password not found in application.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading application.properties", e);
        }
    }

    public static String getDbPassword() {
        return dbPassword;
    }
}
