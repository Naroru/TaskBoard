package chukhlantsev.oleg.taskboard.config;

import chukhlantsev.oleg.taskboard.web.security.JwtTokenFilter;
import chukhlantsev.oleg.taskboard.web.security.JwtTokenProvider;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor_ = @__(@Lazy)) //ленивая инициализация. Без нее будет цикличная зависимость внерения бинов
public class ApplicationConfig {

    private final JwtTokenProvider jwtTokenProvider;

    //бин, которйы будет хэшировать пароль
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //бин, ответственный за управление аутинтификацией
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //при старте приложения будет идти поиск бина типа SecurityFilterChain, который будет определять настройки фильтрации
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //отключаем
                .cors(AbstractHttpConfigurer::disable) //отключаем
                .httpBasic(AbstractHttpConfigurer::disable) //отключаем базовую авторизацию (когда спринг пароль генерит и в консоль выводит)
                .sessionManagement(sessionManagement ->
                        sessionManagement.
                                sessionCreationPolicy(SessionCreationPolicy.STATELESS))//сессия без состояния - будем каждый запрос анализировать через всю эту настройку SecurityFilterChain
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, authException) ->  //исключение что пользователь не авторизован
                                        {
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write("Unauthorized");
                                        })//перехватываем исключения
                                .accessDeniedHandler((request, response, accessDeniedException) -> //исключение, что нет доступа
                                {
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.getWriter().write("Unauthorized");
                                }))
                .authorizeHttpRequests(configurer -> {
                    configurer
                            .requestMatchers("api/v1/auth/**")
                            .permitAll()//для этих разрешаем не авторизироваться
                            .requestMatchers("/swagger-ui/**")
                            .permitAll()
                            .requestMatchers("/v3/api-docs/**")
                            .permitAll()
                            .anyRequest().authenticated(); //для всех остальных требуем аутентификацию}) //указываем, какие запросы будут авторизироваться
                })
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class
                ); //делаем, чтобы наш фильтр выполнялся первым


        return httpSecurity.build();

    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                //в случае если подключен JWT, то для отправки запросов, соответсвенно, требуется JWT токен
                //для возможности его добавления конфигурируем настройки безопасности
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                //доп инфа, выводимая на странице сваггера
                .info(new Info()
                        .title("Task list API")
                        .description("Demo Spring Boot application")
                        .version("1.0")
                );
    }
}
