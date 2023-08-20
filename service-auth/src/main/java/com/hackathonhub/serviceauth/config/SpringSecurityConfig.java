package com.hackathonhub.serviceauth.config;

import com.hackathonhub.serviceauth.config.jwt.AuthFilter;
import com.hackathonhub.serviceauth.config.jwt.AuthTokenValidationFilter;
import com.hackathonhub.serviceauth.exceptions.AccessDeniedExceptionHandler;
import com.hackathonhub.serviceauth.exceptions.AuthEntryPoint;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceauth.utils.JWTUtils;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public SpringSecurityConfig(UserDetailsServiceImpl userDetailsService,
                                AuthEntryPoint authEntryPoint,
                                AccessDeniedExceptionHandler accessDeniedExceptionHandler,
                                JWTUtils jwtUtils, AuthRepository authRepository) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedExceptionHandler = accessDeniedExceptionHandler;
        this.jwtUtils = jwtUtils;
        this.authRepository = authRepository;
        this.bcryptPasswordEncoder = new BcryptPasswordEncoder();
    }

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPoint authEntryPoint;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final JWTUtils jwtUtils;
    private final BcryptPasswordEncoder bcryptPasswordEncoder;
    private final AuthRepository authRepository;


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
                .authorizeHttpRequests(
                        r -> r
                                .requestMatchers("/api/login",
                                        "/api/registration",
                                        "/actuator/info").permitAll()
                                .requestMatchers("/api/test").hasRole("USER")
                                .requestMatchers("/api/admintest").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new AuthTokenValidationFilter(jwtUtils, authRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthFilter(jwtUtils, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);


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
