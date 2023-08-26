package com.hackathonhub.serviceauth.dtos;

import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Builder
@Data
public class ApiAuthResponse {

   private HttpStatus status;
   private String message;
   private ApiResponseDataContext data;
}
