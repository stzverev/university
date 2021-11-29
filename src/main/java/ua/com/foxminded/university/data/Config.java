package ua.com.foxminded.university.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ua.com.foxminded.university.data.service.PropertyReader;

@Configuration
@ComponentScan
@PropertySource("classpath:config.properties")
public class Config {

    @Bean
    public DataSource dataSource(@Value("${driver}") String driverClassName,
            @Value("${url}") String url,
            @Value("${user}") String username,
            @Value("${password}") String password) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public PropertyReader queryReader() {
        return new PropertyReader("queries.properties");
    }

}
