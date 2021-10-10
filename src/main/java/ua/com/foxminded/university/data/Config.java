package ua.com.foxminded.university.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("ua.com.foxminded.university.data")
@PropertySource("classpath:queries.properties")
@PropertySource("classpath:config.properties")
public class Config {

    @Bean
    public DataSource dataSource(@Value("${driver}") String driverClassName,
            @Value("${url}") String url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        return dataSource;
    }

}
