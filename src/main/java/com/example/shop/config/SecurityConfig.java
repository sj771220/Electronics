package com.example.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/login", "/letsignup" , "/main", "/shop/**", "/category/**", "/single/**","/searchResult",
                        "/css/**", "/js/**", "/img/**","/checkpd", "/search/**",
                        "/","/signup","/checkid","/checkmail","/mypageSearch","/checktel","/signupre","/modalcheck",
                        "/updateDefaultAdd","/passwordCh").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();


        // login 설정
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth")
                .usernameParameter("userid")
                .passwordParameter("password")
                .defaultSuccessUrl("/main");

        // logout 설정
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/main");

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }


}
