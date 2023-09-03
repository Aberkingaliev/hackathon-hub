package com.hackathonhub.serviceauth.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


@Builder
@Data
public class ApiAuthResponse<T extends Serializable> {

   private HttpStatus status;
   private String message;
   private T data;
}
