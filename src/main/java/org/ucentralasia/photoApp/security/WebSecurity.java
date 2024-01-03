package org.ucentralasia.photoApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ucentralasia.photoApp.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public WebSecurity(UserService userServiceDetails, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userServiceDetails;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Customize Login URL path
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
                        .permitAll()
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilter(authenticationFilter)
                .addFilter(new AuthorizationFilter(authenticationManager))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}





















