package com.hackathonhub.serviceauth.dtos.contexts;

import com.hackathonhub.serviceauth.models.AuthToken;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
public class ApiResponseDataContext {

    Optional<AuthToken> data;

}
