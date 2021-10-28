package ua.com.foxminded.university.data.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ua.com.foxminded.university.data.PropertyFile;
import ua.com.foxminded.university.exceptions.PropertyIOException;
import ua.com.foxminded.university.exceptions.PropertyNotFoundException;

public class PropertyReader {


    private PropertyReader() {
    }

    public static Properties getProperties(PropertyFile propertyFile) {
        Properties properties = new Properties();
        String fileName = propertyFile.name;
        InputStream propertiesStream = PropertyReader.class
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

    private static void loadProperties(Properties properties,
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
