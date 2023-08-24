package com.hackathonhub.serviceuser.services;

import com.hackathonhub.serviceuser.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.__mocks__.UserMockTestBase;
import com.hackathonhub.serviceuser.__mocks__.UserMocksFactory;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest extends UserMockTestBase {

    @InjectMocks
    private UserService userService = new UserService();

    @Mock
    private UserRepository userRepository;


    @Test
    public void saveUser_Test () {
        /*
               GIVEN
         */
        UserMockStrategy userSaveMockStrategy = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.saveUser);

        UserGrpcService.UserRequest request = userSaveMockStrategy.getRequest();
        UserGrpcService.UserResponse response = userSaveMockStrategy.getResponse();
        User savedUserFromDb = userSaveMockStrategy.getUser(MockLocalUserDataType.USER_FROM_DB);

        StreamRecorder<UserGrpcService.UserResponse> responseObserver = StreamRecorder.create();

        /*
               MOCKS SETTING
         */

        doReturn(savedUserFromDb).when(userRepository).save(any(User.class));


        /*
               EXECUTE
         */

        userService.saveUser(request, responseObserver);

        /*
               VERIFY AFTER
         */

        verify(userRepository).save(any(User.class));

        /*
               RESULT FROM RESPONSE OBSERVER
         */

        List<UserGrpcService.UserResponse> responseFromSaveUser = responseObserver.getValues();

        /*
               ASSERTIONS
         */

        Assertions.assertEquals(1, responseFromSaveUser.size());
        Assertions.assertEquals(responseFromSaveUser.get(0), response);
    }

    @Test
    public void getUserByEmail_Test () {
        /*
               GIVEN
         */

        UserMockStrategy userGetByEmailMockStrategy = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.getUserByEmail);

        UserGrpcService.UserRequest request = userGetByEmailMockStrategy.getRequest();
        UserGrpcService.UserResponse response = userGetByEmailMockStrategy.getResponse();
        User foundedUser = userGetByEmailMockStrategy.getUser(MockLocalUserDataType.USER_FROM_DB);

        StreamRecorder<UserGrpcService.UserResponse> responseObserver = StreamRecorder.create();

        /*
               MOCKS SETTING
         */

        doReturn(foundedUser).when(userRepository).getByEmail(any(String.class));

        /*
               EXECUTE
         */

        userService.getUserByEmail(request, responseObserver);

        /*
               VERIFY AFTER
         */

        verify(userRepository).getByEmail(any(String.class));

        /*
               RESULT FROM RESPONSE OBSERVER
         */

        List<UserGrpcService.UserResponse> responseFromGetByEmail = responseObserver.getValues();

        /*
               ASSERTIONS
         */

        Assertions.assertEquals(1, responseFromGetByEmail.size());
        Assertions.assertEquals(responseFromGetByEmail.get(0), response);
    }

    @Test
    public void getDeleteUserById_Test () {
        /*
               GIVEN
         */

        UserMockStrategy userDeleteByIdMockStrategy = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.deleteUser);

        UserGrpcService.UserRequest request = userDeleteByIdMockStrategy.getRequest();
        UserGrpcService.UserResponse response = userDeleteByIdMockStrategy.getResponse();


        StreamRecorder<UserGrpcService.UserResponse> responseObserver = StreamRecorder.create();

        /*
               MOCKS SETTING
         */

        doNothing().when(userRepository).deleteById(any(UUID.class));

        /*
               EXECUTE
         */

        userService.deleteUser(request, responseObserver);

        /*
               VERIFY AFTER
         */

        verify(userRepository).deleteById(any(UUID.class));

        /*
               RESULT FROM RESPONSE OBSERVER
         */

        List<UserGrpcService.UserResponse> responseFromDeleteById = responseObserver.getValues();

        /*
               ASSERTIONS
         */

        Assertions.assertEquals(1, responseFromDeleteById.size());
        Assertions.assertEquals(responseFromDeleteById.get(0), response);
    }

    @Test
    public void isExistByEmail_Test () {
        /*
               GIVEN
         */

        UserMockStrategy userIsExistByEmailStrategy = UserMocksFactory
                .getMockStrategy(UserGrpcService.actions_enum.isExistUserByEmail);

        UserGrpcService.UserRequest request = userIsExistByEmailStrategy.getRequest();
        UserGrpcService.UserResponse response = userIsExistByEmailStrategy.getResponse();


        StreamRecorder<UserGrpcService.UserResponse> responseObserver = StreamRecorder.create();

        /*
               MOCKS SETTING
         */

        doReturn(true).when(userRepository).existByEmail(any(String.class));

        /*
               EXECUTE
         */

        userService.isExistUserByEmail(request, responseObserver);

        /*
               VERIFY AFTER
         */

        verify(userRepository).existByEmail(any(String.class));

        /*
               RESULT FROM RESPONSE OBSERVER
         */

        List<UserGrpcService.UserResponse> responseFromIsExistByEmail = responseObserver.getValues();

        /*
               ASSERTIONS
         */

        Assertions.assertEquals(1, responseFromIsExistByEmail.size());
        Assertions.assertEquals(responseFromIsExistByEmail.get(0), response);
    }


}