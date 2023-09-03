package com.hackathonhub.serviceauth.controllers;

import com.hackathonhub.serviceauth.constants.AuthApiResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.hackathonhub.serviceauth.services.RegistrationService;
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
    public ResponseEntity<ApiAuthResponse<User>> registration(@RequestBody User user) {
        ApiAuthResponse<User> response = registrationService.registration(user);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse<AuthToken>> login(
            @RequestBody UserLoginRequest loginRequest,
            HttpServletResponse responseServlet) {
        ApiAuthResponse<AuthToken> response = loginService.login(loginRequest);
        if (response.getStatus() == HttpStatus.OK) {
            Cookie refreshTokenCookie = new Cookie("refreshToken",
                    response.getData().getRefreshToken());

            refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toMillis(30));
            refreshTokenCookie.setPath("/");

            responseServlet.addCookie(refreshTokenCookie);

            responseServlet.setHeader(HttpHeaders.AUTHORIZATION,
                    "Bearer " + response.getData().getAccessToken());
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
                        .message(AuthApiResponseMessage.USER_SUCCESS_LOGOUT)
                        .build());
    }

}
