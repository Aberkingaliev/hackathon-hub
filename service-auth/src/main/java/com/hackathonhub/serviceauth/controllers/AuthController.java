package com.hackathonhub.serviceauth.controllers;


import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.LoginService;
import com.hackathonhub.serviceauth.services.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class AuthController {


    private final RegistrationService registrationService;

    private final LoginService loginService;

    public AuthController(final RegistrationService registrationService,
                          final LoginService loginService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
    }


    @PostMapping("/registration")
    public ApiAuthResponse registration(@RequestBody User user) {

        return registrationService.registration(user);
    }

    @PostMapping("/login")
    private ApiAuthResponse login(@RequestBody UserLoginRequest loginRequest,
                                  HttpServletResponse responseServlet) {
        ApiAuthResponse response = loginService.login(loginRequest);

        if (response.getStatus() == HttpStatus.OK) {
            Cookie cookie = new Cookie("refreshToken",
                    response
                            .getData()
                            .getData()
                            .get()
                            .getRefreshToken());

            responseServlet.addCookie(cookie);

            responseServlet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + response
                    .getData()
                    .getData()
                    .get()
                    .getAccessToken());
        }

        return response;
    }


    @GetMapping("/test")
    private String test() {
        return "TEST ROLE USER";
    }

    @GetMapping("/admintest")
    private String adminreq() {
        return "TEST ROLE ADMIN";
    }

}
