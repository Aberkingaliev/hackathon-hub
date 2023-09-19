package com.hackathonhub.servicecontest.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


@Builder
@Getter
public class ApiAuthResponse<T extends Serializable>{
    private HttpStatus status;
    private String message;
    private T data;
}
