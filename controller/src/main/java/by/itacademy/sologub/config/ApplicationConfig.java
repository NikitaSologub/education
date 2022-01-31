package by.itacademy.sologub.config;

import by.itacademy.sologub.controllers.interceptors.HiddenMethodInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@ComponentScan("by.itacademy.sologub")
@PropertySource("classpath:db_config.properties")
@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
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

    @Override // ресурсы для работы css и картинок
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(CSS_PATH)
                .addResourceLocations(CSS_LOCATION);
        registry.addResourceHandler(IMG_PATH)
                .addResourceLocations(IMG_LOCATION);
    }

    @Override // добавление перехватчика скрытого метода
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor());
    }

    @Bean // создание перехватчика скрытого метода
    HiddenMethodInterceptor loggingInterceptor() {
        return new HiddenMethodInterceptor();
    }

    @Bean // определение ViewResolver для jsp
    public InternalResourceViewResolver internalResourceViewResolver(@Autowired ApplicationContext ctx) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setApplicationContext(ctx);
        resolver.setPrefix(VIEW_PREFIX);
        resolver.setSuffix(VIEW_POSTFIX);
        return resolver;
    }

    @Bean // DataSource класс (pool connection)
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

    @Bean //SessionFactory - для работы с HibernateRepo
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().configure();
        return cfg.buildSessionFactory();
    }

    @Bean // для LocalContainerEntityManagerFactoryBean (ресурсы уточняющие параметры соединения с БД)
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.setProperty("show_sql", "true");
        properties.setProperty("hbm2ddl.auto", "none");
        properties.setProperty("current_session_context_class", "thread");
        properties.setProperty("connection.pool_size", "20");
        properties.setProperty("hibernate.dbcp.initialSize", "5");
        properties.setProperty("hibernate.dbcp.maxTotal", "20");
        properties.setProperty("hibernate.dbcp.maxIdle", "10");
        properties.setProperty("hibernate.dbcp.minIdle", "5");
        properties.setProperty("hibernate.dbcp.maxWaitMillis", "-1");
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        properties.setProperty("connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("connection.url", "jdbc:postgresql://localhost:5432/edu");
        properties.setProperty("connection.username", "wombat");
        properties.setProperty("connection.password", "wombat");
        return properties;
    }

    @Bean // EntityManagerFactory - для работы с SpringOrmRepo
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired Properties jpaProperties, @Autowired DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emFactoryBean.setDataSource(dataSource);
        emFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emFactoryBean.setPackagesToScan("by.itacademy.sologub.model");
        emFactoryBean.setJpaProperties(jpaProperties);
        return emFactoryBean;
    }

    @Bean // TransactionManager - для работы с SpringOrmRepo
    PlatformTransactionManager platformTransactionManager(@Qualifier("entityManagerFactory") @Autowired EntityManagerFactory emf) {
        JpaTransactionManager jpaTxManager = new JpaTransactionManager();
        jpaTxManager.setEntityManagerFactory(emf);
        return jpaTxManager;
    }
}