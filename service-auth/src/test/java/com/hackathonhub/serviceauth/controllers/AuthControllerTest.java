package com.hackathonhub.serviceauth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.hackathonhub.serviceauth.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceauth.__mocks__.UserMocksFactory;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.LoginService;
import com.hackathonhub.serviceauth.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapperWithJdkModule;

    @Mock
    private RegistrationService registrationService;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        loginService = new LoginService();
        registrationService = new RegistrationService();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
        mapperWithJdkModule = new ObjectMapper().registerModule(new Jdk8Module());
    }

    @Test
    void registration_Test () throws Exception {
        /*

        GIVEN

         */
        User userForRegistration = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.saveUser)
                .getUser(MockLocalUserDataType.MAPPED_USER_FROM_REQUEST);

        User userAfterSave = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.saveUser)
                .getUser(MockLocalUserDataType.MAPPED_USER_FROM_RESPONSE);

        String userForRegistrationJson = mapperWithJdkModule.writeValueAsString(userForRegistration);

        ApiResponseDataContext expectedResponseDataContext = ApiResponseDataContext
                .builder()
                .user(Optional.of(userAfterSave))
                .build();

        ApiAuthResponse expectedResponse = ApiAuthResponse
                .builder()
                .status(HttpStatus.CREATED)
                .message("USER_SUCCESSFULY_REGISTRED")
                .data(expectedResponseDataContext)
                .build();

        String responseAfterRegistrationJson = mapperWithJdkModule.writeValueAsString(expectedResponse);

        /*

        MOCKS SETTING

         */

        when(registrationService.registration(any(User.class))).thenReturn(expectedResponse);

        /*

        EXECUTE AND ASSERTIONS

         */

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userForRegistrationJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseAfterRegistrationJson));
    }

    @Test
    void login_Test() throws Exception {
        /*

        GIVEN

         */

        UserLoginRequest userLoginRequest = UserLoginRequest
                .builder()
                    .email("testov@gmail.com")
                    .password("testBigPasswordGif9Da_@9dZZ")
                .build();
        String userLoginRequestJson = mapperWithJdkModule.writeValueAsString(userLoginRequest);

        AuthToken tokensForTest = new AuthToken().setAccessToken("access").setRefreshToken("refresh");

        ApiResponseDataContext expectedResponseDataContext = ApiResponseDataContext
                .builder()
                .data(Optional.of(tokensForTest))
                .build();

        ApiAuthResponse expectedResponse = ApiAuthResponse
                .builder()
                .status(HttpStatus.OK)
                .message("USER_SUCCESS_AUTHORIZED")
                .data(expectedResponseDataContext)
                .build();

        String responseAfterLogin = mapperWithJdkModule.writeValueAsString(expectedResponse);

        /*

        SETTING MOCKS

         */

        when(loginService.login(any(UserLoginRequest.class))).thenReturn(expectedResponse);

        /*

        EXECUTE AND ASSERTIONS

         */


        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Authorization", "Bearer " + tokensForTest.getAccessToken()))
                .andExpect(cookie().value("refreshToken", tokensForTest.getRefreshToken()))
                .andExpect(content().json(responseAfterLogin));
    }

    @Test
    void logout_Test () throws Exception {
        /*

        GIVEN

        */

        String responseAfterLogoutJson = mapperWithJdkModule.writeValueAsString(
                ApiAuthResponse
                        .builder()
                        .status(HttpStatus.OK)
                        .message("LOGOUT_SUCCESSFUL")
                        .build()
        );

        /*

        EXECUTE AND ASSERTIONS

        */

        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseAfterLogoutJson))
                .andExpect(header().string("Authorization", ""))
                .andExpect(cookie().value("refreshToken", ""));
    }

}
