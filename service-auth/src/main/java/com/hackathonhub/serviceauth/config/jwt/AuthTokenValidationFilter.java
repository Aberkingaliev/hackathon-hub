package com.hackathonhub.serviceauth.config.jwt;


import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.utils.CustomRequestHeaderUtil;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;


@Slf4j
public class AuthTokenValidationFilter extends GenericFilterBean {

    public AuthTokenValidationFilter(final JWTUtils jwtUtils,
                                     final AuthRepository authRepository) {
        this.jwtUtils = jwtUtils;
        this.authRepository = authRepository;
    }

    private final JWTUtils jwtUtils;

    private final AuthRepository authRepository;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest currentRequest = (HttpServletRequest) request;

            String accessToken = jwtUtils.parseAccessToken(currentRequest);
            Cookie refreshToken = getRefreshTokenFromCookies(
                    currentRequest.getCookies(),
                    "refreshToken"
            );
            if (refreshToken != null && accessToken != null) {

                String refreshTokenValue = refreshToken.getValue();

                if (!jwtUtils.validateToken(accessToken)
                        && jwtUtils.validateToken(refreshTokenValue)) {

                    String email = jwtUtils.getUserSubject(refreshTokenValue);

                    AuthToken savedToken = updateAndSaveTokens(email, refreshTokenValue);

                    HttpServletRequestWrapper newRequest = getUpdatedServletRequest(
                                    (HttpServletRequest) request,
                                    savedToken);

                    HttpServletResponse currentResponse = (HttpServletResponse) response;

                    currentResponse.addHeader(
                            "Authorization",
                            "Bearer " + savedToken.getAccessToken());

                    currentResponse.addCookie(
                            new Cookie("refreshToken",
                            savedToken.getRefreshToken()));

                    chain.doFilter(newRequest, currentResponse);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("Error during validation token "
                    + e.getMessage());
        }

        chain.doFilter(request, response);

    }

    private AuthToken updateAndSaveTokens(String email,
                                          String currentRefresh) {
        HashMap<String, String> generatedTokens =
                jwtUtils.generateToken(email);

        AuthToken authToken = authRepository.findByRefreshToken(currentRefresh);

        authToken
                .setRefreshToken(generatedTokens.get("refreshToken"))
                .setAccessToken(generatedTokens.get("accessToken"));

        return authRepository
                .save(authToken);
    }

    private static HttpServletRequestWrapper getUpdatedServletRequest(
             HttpServletRequest request,
             AuthToken savedToken) {
        HashMap<String, String> newTokensForHeader = new HashMap<>(
                Map.ofEntries(
                        Map.entry("Authorization",
                                "Bearer " + savedToken.getAccessToken()
                        )
                )
        );
        List<Cookie> newCookiesList = List.of(
                new Cookie("refreshToken",
                savedToken.getRefreshToken())
        );

        return new CustomRequestHeaderUtil(
                request,
                newTokensForHeader,
                newCookiesList
        );
    }

    public Cookie getRefreshTokenFromCookies(Cookie[] cookies,
                                             String key) {
        if (cookies != null) {
            Optional<Cookie> foundedCookie = Arrays.stream(cookies)
                    .filter(cookie -> key.equals(cookie.getName()))
                    .findFirst();

            return foundedCookie.orElse(null);
        }

        return null;
    }
}
