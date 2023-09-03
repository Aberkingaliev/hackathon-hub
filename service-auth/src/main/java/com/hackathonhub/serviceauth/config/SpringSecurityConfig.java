package com.hackathonhub.serviceauth.config;

import com.hackathonhub.serviceauth.exceptions.AccessDeniedExceptionHandler;
import com.hackathonhub.serviceauth.exceptions.AuthEntryPoint;
import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public SpringSecurityConfig(UserDetailsServiceImpl userDetailsService,
                                AuthEntryPoint authEntryPoint,
                                AccessDeniedExceptionHandler accessDeniedExceptionHandler) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedExceptionHandler = accessDeniedExceptionHandler;
        this.bcryptPasswordEncoder = new BcryptPasswordEncoder();
    }

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPoint authEntryPoint;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final BcryptPasswordEncoder bcryptPasswordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        a -> a.authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedExceptionHandler)
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(
                        authenticationManager(userDetailsService, bcryptPasswordEncoder)
                )
                .authorizeHttpRequests().antMatchers("/api/login", "/api/registration").permitAll();


        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BcryptPasswordEncoder passwordEncoder) throws Exception {
        return authentication -> {
            String email = authentication.getPrincipal().toString();
            String password = authentication.getCredentials().toString();

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            };

            return new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    userDetails.getAuthorities());
        };
    }


}
