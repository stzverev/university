package ua.com.foxminded.university.data;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;

import ua.com.foxminded.university.data.service.PropertyReader;

@Configuration
@ComponentScan
@PropertySource("classpath:jndi.properties")
public class Config {

    @Bean
    @Autowired
    public DataSource dataSource(Environment environment) throws NamingException {
        return (DataSource) new JndiTemplate().lookup(environment.getProperty("url"));
    }

    @Bean
    public PropertyReader queryReader() {
        return new PropertyReader("queries.properties");
    }

}
