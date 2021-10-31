package ua.com.foxminded.university.data.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ua.com.foxminded.university.exceptions.PropertyIOException;
import ua.com.foxminded.university.exceptions.PropertyNotFoundException;

public class PropertyReader {

    private final String fileName;

    public PropertyReader(String fileName) {
        this.fileName = fileName;
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        InputStream propertiesStream = getClass()
                .getClassLoader().getResourceAsStream(fileName);

        if (propertiesStream != null) {
            loadProperties(properties, propertiesStream);
            return properties;
        } else {
            String message = String.format(
                    "Property file %s not found", fileName);
            throw new PropertyNotFoundException(message);
        }
    }

    private void loadProperties(Properties properties,
            InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            String message = String.format(
                    "Error at runtime loading properties: %s", e.getMessage());
            throw new PropertyIOException(message);
        }
    }

}
