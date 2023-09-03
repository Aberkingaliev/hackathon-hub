package com.hackathonhub.serviceauth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JWTUtilsTest {

    private String SECRET_KEY = "0be2557f-8cdc-4fdb-b15b-c01a5813b9f6";
    private static final long TOKEN_EXPIRATION_TIME = 10000L;
    
    
    private String base64SecretKey;
    private JWTUtils jwtUtils;

    @BeforeEach void setUp() {
        jwtUtils = new JWTUtils();
        base64SecretKey = Base64
                .getEncoder()
                .encodeToString(
                        SECRET_KEY
                                .getBytes(StandardCharsets.UTF_8)
                );
        jwtUtils.setSecretKey(SECRET_KEY);
    }

    @Test
    void getParsedSecretKey_Test() {
        /*

        EXECUTE

         */

        String parsedKey = jwtUtils.getParsedSecretKey();

        /*

        ASSERTIONS

         */

        Assertions.assertEquals(base64SecretKey, parsedKey);

    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        /*

        GIVEN

         */

        String testToken = getValidTestToken("jwtUtilsTest@gmail.com");

        /*

        EXECUTE

         */

        boolean result = jwtUtils.validateToken(testToken);

        /*

        ASSERTIONS

         */

        Assertions.assertTrue(result);
    }

    @Test
    void validateToken_ValidToken_ReturnsFalse() {
        /*

        GIVEN

         */

        String token = getInValidTestToken("jwtUtilsTest@gmail.com");

        /*

        EXECUTE

         */

        boolean result = jwtUtils.validateToken(token);

        /*

        ASSERTIONS

         */

        Assertions.assertFalse(result);
    }

    @Test
    void generateTokens_Test() {
        /*

        GIVEN

         */

        String email = "jwtUtilsTest@gmail.com";
        List<String> roles = List.of("ROLE_USER");
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
                .toList();
        /*

        EXECUTE

         */

        HashMap<String, String> result = jwtUtils.generateToken(email, authorities);

        /*

        ASSERTIONS

         */

        Claims claimsAccess = getClaims(result.get("accessToken"));

        Claims claimsRefresh = getClaims(result.get("refreshToken"));

        Assertions.assertTrue(result.containsKey("accessToken"));
        Assertions.assertTrue(result.containsKey("refreshToken"));
        Assertions.assertTrue(claimsAccess.getExpiration().after(new Date()));
        Assertions.assertTrue(claimsRefresh.getExpiration().after(new Date()));
        Assertions.assertNotNull(result.get("accessToken"));
        Assertions.assertNotNull(result.get("refreshToken"));
        Assertions.assertNotEquals(result.get("accessToken"), result.get("refreshToken"));
        Assertions.assertTrue(result.get("accessToken").contains("."));
        Assertions.assertEquals(email, claimsAccess.getSubject());
        Assertions.assertEquals(email, claimsRefresh.getSubject());

    }

    @Test
    void getUserSubject_Test() {
        /*

        GIVEN

         */

        String email = "jwtUtilTest@gmail.com";
        String testToken = getValidTestToken(email);

        /*

        EXECUTE

         */

        String result = jwtUtils.getUserSubject(testToken);

        /*

        ASSERTIONS

         */

        Claims claimsAccess = getClaims(testToken);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("@"));
        Assertions.assertEquals(email, result);
        Assertions.assertTrue(claimsAccess.getExpiration().after(new Date()));
    }

    @Test
    void parseAccessToken_Test() {
        /*

        GIVEN

         */

        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        String testToken = getValidTestToken("jwtUtilsTest@gmail.com");

        /*

        SETTING MOCKS

        */

        when(mockRequest.getHeader(anyString())).thenReturn("Bearer " + testToken);

        /*

        EXECUTE

        */

        String result = jwtUtils.parseAccessToken(mockRequest);

        /*

        VERIFY

         */

        verify(mockRequest).getHeader(anyString());

        /*

        ASSERTIONS

         */

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("."));
        Assertions.assertEquals(testToken, result);

    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(base64SecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getValidTestToken(String subject) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .compact();
    }

    private String getInValidTestToken(String subject) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() - TOKEN_EXPIRATION_TIME))
                .compact();
    }

}
