package com.hackathonhub.serviceauth.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


@Configuration
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException)
            throws IOException {
        final ApiAuthResponse responseBody = ApiAuthResponse
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .message("NOT_ENOUGH_ACCESS_FOR_RESOURCE")
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");


        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(response.getOutputStream(), responseBody);
    }
}
