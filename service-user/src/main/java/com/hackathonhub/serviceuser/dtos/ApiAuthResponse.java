package com.hackathonhub.serviceuser.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Optional;

@Builder
@Data
@AllArgsConstructor
public class ApiAuthResponse<T extends Serializable> {

   private HttpStatus status;
   private String message;
   @Builder.Default
   private Optional<T> data = Optional.empty();

   public ApiAuthResponse () {}


   public ApiAuthResponse<T> created(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.CREATED)
              .message(message)
              .build();
   }

   public ApiAuthResponse<T> created(T data, String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.CREATED)
              .message(message)
              .data(Optional.of(data))
              .build();
   }


   public ApiAuthResponse<T> ok(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.OK)
              .message(message)
              .build();
   }

   public ApiAuthResponse<T> ok(T data, String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.OK)
              .message(message)
              .data(Optional.of(data))
              .build();
   }

   public ApiAuthResponse<T> notFound(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.NOT_FOUND)
              .message(message)
              .build();
   }

   public ApiAuthResponse<T> badRequest(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.BAD_REQUEST)
              .message(message)
              .build();
   }

   public ApiAuthResponse<T> conflict(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.CONFLICT)
              .message(message)
              .build();
   }

   public ApiAuthResponse<T> internalServerError(String message) {
      return ApiAuthResponse.<T>builder()
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .message(message)
              .build();
   }
}
