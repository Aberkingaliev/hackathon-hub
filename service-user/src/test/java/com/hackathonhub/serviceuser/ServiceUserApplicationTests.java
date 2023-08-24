package com.hackathonhub.serviceuser;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.UserGetByEmailMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserSaveMapper;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceUserApplicationTests {


	@Test
	void contextLoads() {
	}

}
