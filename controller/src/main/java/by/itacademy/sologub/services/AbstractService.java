package by.itacademy.sologub.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:db_config.properties")
public abstract class AbstractService {
    protected static final String SUFFIX = "Impl";
    @Value("${type}")
    protected String type;
}