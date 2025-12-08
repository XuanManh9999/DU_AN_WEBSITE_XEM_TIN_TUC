package com.datn.website_xem_tin_tuc.config.security;

import com.datn.website_xem_tin_tuc.service.ManageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {
    private final ManageUserService manageUserService;
    private final PreFilterSecurity preFilterSecurity;
    private final SecurityBeansConfig securityBeansConfig;
    private String [] WHITE_LIST = {"/api/v1/auth/**", "/api/v1/payment/**", "/api/v1/posts/**", "/api/v1/category-posts/**", "/api/v1/category-posts/by-category/**", "/api/v1/category/**", "/api/v1/articles/**", "/api/v1/products/**", "/api/v1/address/**"};

    // config cors giup trinh duyet xac dinh cac origin nao duoc phep truy cap
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://admin-365-news.vercel.app", "https://365news.vercel.app", "http://localhost:5173", "http://localhost:5174")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }

    // tao mot password encoder dung de ma hoa mat khau
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider())
                .addFilterBefore(preFilterSecurity, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(securityBeansConfig.passwordEncoder());
        provider.setUserDetailsService(manageUserService.userDetailsService());
        return provider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> {
            webSecurity.ignoring()
                    .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui/**", "/swagger-resources/**");
        };
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
