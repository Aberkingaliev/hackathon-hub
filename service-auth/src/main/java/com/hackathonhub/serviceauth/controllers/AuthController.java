package com.hackathonhub.serviceauth.controllers;

import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.LoginService;
import com.hackathonhub.serviceauth.services.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private LoginService loginService;


    @PostMapping("/registration")
    public ResponseEntity<ApiAuthResponse> registration(@RequestBody User user) {
        ApiAuthResponse response = registrationService.registration(user);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> login(@RequestBody UserLoginRequest loginRequest,
                                  HttpServletResponse responseServlet) {
        ApiAuthResponse response = loginService.login(loginRequest);
        if (response.getStatus() == HttpStatus.OK) {
            Cookie refreshTokenCookie = new Cookie("refreshToken",
                    response
                            .getData()
                            .getData()
                            .get()
                            .getRefreshToken());
            refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toMillis(30));

            responseServlet.addCookie(refreshTokenCookie);

            responseServlet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + response
                    .getData()
                    .getData()
                    .get()
                    .getAccessToken());
        }

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiAuthResponse> logout(HttpServletResponse responseServlet) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setMaxAge(0);
        responseServlet.addCookie(refreshTokenCookie);
        responseServlet.setHeader("Authorization", "");

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiAuthResponse
                        .builder()
                        .status(HttpStatus.OK)
                        .message("LOGOUT_SUCCESSFUL")
                        .build());
    }

}
