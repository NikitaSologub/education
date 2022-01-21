package by.itacademy.sologub.config;

import by.itacademy.sologub.controllers.interceptors.HiddenMethodInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.beans.PropertyVetoException;

@ComponentScan("by.itacademy.sologub")
@PropertySource("classpath:db_config.properties")
@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig implements WebMvcConfigurer {
    private static final String VIEW_PREFIX = "/view/";
    private static final String VIEW_POSTFIX = ".jsp";
    private static final String CSS_PATH = "/css/**";
    private static final String CSS_LOCATION = "/css/";
    private static final String IMG_PATH = "/img/**";
    private static final String IMG_LOCATION = "/img/";
    @Value("${driver}")
    String driver;
    @Value("${url}")
    String url;
    @Value("${login}")
    String login;
    @Value("${password}")
    String password;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(CSS_PATH)
                .addResourceLocations(CSS_LOCATION);
        registry.addResourceHandler(IMG_PATH)
                .addResourceLocations(IMG_LOCATION);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor());
    }

    @Bean
    HiddenMethodInterceptor loggingInterceptor() {
        return new HiddenMethodInterceptor();
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver(@Autowired ApplicationContext ctx) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setApplicationContext(ctx);
        resolver.setPrefix(VIEW_PREFIX);
        resolver.setSuffix(VIEW_POSTFIX);
        return resolver;
    }

    @Bean
    public ComboPooledDataSource comboPooledDataSource() throws PropertyVetoException {
        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass(driver);
        pool.setJdbcUrl(url);
        pool.setUser(login);
        pool.setPassword(password);
        pool.setInitialPoolSize(1);
        pool.setMinPoolSize(1);
        pool.setAcquireIncrement(1);
        pool.setMaxPoolSize(5);
        pool.setMaxStatements(100);
        return pool;
    }

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().configure();
        return cfg.buildSessionFactory();
    }
}