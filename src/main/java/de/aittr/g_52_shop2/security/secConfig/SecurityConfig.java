package de.aittr.g_52_shop2.security.secConfig;

import de.aittr.g_52_shop2.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Этот метод отключает защиту CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Этот метод служит для настройки сессий.
                // При текущей настройке мы сессии отключили,
                // то есть сервер будет просить логин/пароль при каждом запросе
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // отключаем базовую авторизацию
                .httpBasic(AbstractHttpConfigurer::disable)
                // При помощи этого метода мы конфигурируем доступ к разному функционалу
                // приложения для разных ролей пользователей
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/customers/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/customers/").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/customers").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/confirm/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}


