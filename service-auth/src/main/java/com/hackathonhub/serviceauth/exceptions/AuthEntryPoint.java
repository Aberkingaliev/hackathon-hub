package com.hackathonhub.serviceauth.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


@Slf4j
@Configuration
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error("Authentication error occurred " + authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ApiAuthResponse responseBody = ApiAuthResponse
                .builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(authException.getMessage())
                .build();

        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(response.getOutputStream(), responseBody);
    }
}
