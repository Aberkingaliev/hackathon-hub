package com.hackathonhub.serviceauth.controllers;


import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ApiAuthResponse registration(@RequestBody User user, HttpServletResponse responseServlet) {
        ApiAuthResponse response = authService.registration(user);

        if (response.getStatus() == HttpStatus.CREATED) {
            Cookie cookie = new Cookie("refreshToken",
                    response
                            .getData()
                            .getData()
                            .get()
                            .getRefreshToken());

            responseServlet.addCookie(cookie);
        }

        return response;
    }

}
