package chukhlantsev.oleg.taskboard.config;

import chukhlantsev.oleg.taskboard.web.security.JwtTokenFilter;
import chukhlantsev.oleg.taskboard.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationConfig {

    private final JwtTokenProvider jwtTokenProvider;

    //бин, которйы будет хэшировать пароль
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //бин, ответственный за управление авторизацией
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //при старте приложения будет идти поиск бина типа SecurityFilterChain, который будет определять настройки фильтрации
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable() //отключаем
                .cors() //подключаем
                .and()
                .httpBasic().disable() //отключаем базовую авторизацию (когда спринг пароль генерит и в консоль выводит)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //сессия без состояния - будем каждый запрос анализировать через всю эту настройку SecurityFilterChain
                .and()
                .exceptionHandling() //перехватываем исключения
                .authenticationEntryPoint((request, response, authException) ->  //исключение что пользователь не авторизован
                {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> //исключение, что нет доступа
                {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Unauthorized");
                })
                .and()
                .authorizeHttpRequests() //указываем, какие запросы будут авторизироваться
                .requestMatchers("api/v1/auth/**").permitAll() //для этих разрешаем не авторизироваться
                .anyRequest().authenticated() //для всех остальных требуем аутентификацию
                .and()
                .anonymous().disable()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); //делаем, чтобы наш фильтр выполнялся первым

        return httpSecurity.build();

    }
