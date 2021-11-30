package ua.com.foxminded.university.data.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private DataSource dataSource;

    public void loadData() {
        ResourceDatabasePopulator resourceDatabasePopulator = new
                ResourceDatabasePopulator(false, false, "UTF-8",
                        new ClassPathResource("data.sql"));
        resourceDatabasePopulator.execute(dataSource);
    }
}
