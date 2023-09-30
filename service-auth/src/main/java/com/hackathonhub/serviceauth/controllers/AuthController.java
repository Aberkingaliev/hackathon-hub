package com.hackathonhub.serviceauth.controllers;

import com.hackathonhub.serviceauth.constants.ApiAuthResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserCreateDto;
import com.hackathonhub.serviceauth.dtos.UserDto;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.services.LoginService;
import com.hackathonhub.serviceauth.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private LoginService loginService;


    @Operation(summary = "User registration")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "User created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                        content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<ApiAuthResponse<UserDto>> registration(@RequestBody UserCreateDto user) {
        ApiAuthResponse<UserDto> response = registrationService.registration(user);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Login")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "User authorized"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse<AuthToken>> login(
            @RequestBody UserLoginRequest loginRequest,
            HttpServletResponse responseServlet) {
        ApiAuthResponse<AuthToken> response = loginService.login(loginRequest);

        if (response.getStatus() == HttpStatus.OK) {
            response.getData().ifPresent(authToken -> {
                Cookie refreshTokenCookie = new Cookie("refreshToken",
                        authToken.getRefreshToken());

                refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toMillis(30));
                refreshTokenCookie.setPath("/");

                responseServlet.addCookie(refreshTokenCookie);

                responseServlet.setHeader(HttpHeaders.AUTHORIZATION,
                        "Bearer " + authToken.getAccessToken());
            });
        }

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Logout")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "User logged out"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiAuthResponse> logout(HttpServletResponse responseServlet) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setMaxAge(0);
        responseServlet.addCookie(refreshTokenCookie);
        responseServlet.setHeader("Authorization", "");

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiAuthResponse<>()
                        .ok(ApiAuthResponseMessage.USER_SUCCESS_LOGGED_OUT));
    }

}
