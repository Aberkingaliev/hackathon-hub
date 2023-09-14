package com.hackathonhub.serviceauth.constants;

public class ApiAuthResponseMessage {

    public static final String USER_SUCCESS_AUTHORIZED = "USER_SUCCESS_AUTHORIZED";
    public static final String USER_SUCCESS_REGISTERED = "USER_SUCCESS_REGISTERED";
    public static final String USER_ALREADY_REGISTERED = "USER_ALREADY_REGISTERED";
    public static final String USER_SUCCESS_LOGGED_OUT = "USER_SUCCESS_LOGGED_OUT";

    public static String registrationFailed (String message) {
        return "USER_REGISTRATION_FAILED " + message;
    }

    public static String authenticationFailed (String message) {
        return "USER_AUTHENTICATION_FAILED " + message;
    }
}
