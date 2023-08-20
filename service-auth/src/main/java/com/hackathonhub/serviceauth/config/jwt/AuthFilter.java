package com.hackathonhub.serviceauth.config.jwt;


import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;


@Slf4j
public class AuthFilter extends GenericFilterBean {
    private final JWTUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthFilter(JWTUtils jwtUtils,
                      UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        try {
            String accessToken = jwtUtils
                    .parseAccessToken((HttpServletRequest) request);

            String email = jwtUtils.getUserSubject(accessToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken processedToken =
                    new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext()
                    .setAuthentication(processedToken);

        } catch (Exception e) {
            log.error("Error during authorize "
                    + e.getMessage());
        }


        chain.doFilter(request, response);
    }

}
