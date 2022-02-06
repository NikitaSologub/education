package by.itacademy.sologub.config;

import by.itacademy.sologub.config.auth_checker.UrlRedirectAfterAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UrlRedirectAfterAuthenticationSuccessHandler urlRedirectHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, @Autowired UserDetailsService service) throws Exception {
        auth.userDetailsService(service);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//разрешаем доступ сторонних ресурсов (css или картинки) к нашим jsp
                .httpBasic().and()//предлагаем базовый вариант авторизации
                .authorizeRequests()// АВТОРИЗОВАТЬ ЗАПРОС
                .antMatchers("/view/admin_*").hasRole("ADMIN")//на эти ресурсы можно только админу
                .antMatchers("/groups/**", "/subjects/**").hasRole("ADMIN")//только для админа
                .antMatchers("/students/**", "/teachers/**").hasRole("ADMIN")//только для админа
                .antMatchers("/view/teacher_*").hasRole("TEACHER")//только для учителя
                .antMatchers("/view/student_*").hasRole("STUDENT")//только для студента
                .antMatchers("/rest/**").authenticated()//доступ всем кто прошел аутентификацию
                .and().formLogin()//получаем в браузере базовую страницу входа
                .successHandler(urlRedirectHandler)//если знаем роль то перейдем на нужную страницу
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Autowired UserDetailsService userService,
                                                               @Autowired PasswordEncoder encoder) {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userService);
        daoAuthProvider.setPasswordEncoder(encoder);
        return daoAuthProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}