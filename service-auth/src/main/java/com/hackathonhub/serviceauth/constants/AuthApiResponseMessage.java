package com.hackathonhub.serviceauth.constants;

public class AuthApiResponseMessage {

    public static final String USER_SUCCESS_AUTHORIZED = "USER_SUCCESS_AUTHORIZED";
    public static final String USER_SUCCESS_REGISTERED = "USER_SUCCESS_REGISTERED";
    public static final String USER_ALREADY_REGISTRED = "USER_ALREADY_REGISTRED";
    public static final String USER_SUCCESS_LOGOUT = "USER_SUCCESS_LOGOUT";

    public static String registrationFailed (String message) {
        return "USER_REGISTRATION_FAILED " + message;
    }

    public static String authenticationFailed (String message) {
        return "USER_AUTHENTICATION_FAILED " + message;
    }
}
