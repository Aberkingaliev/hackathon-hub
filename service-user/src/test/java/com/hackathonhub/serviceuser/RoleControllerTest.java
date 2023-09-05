package com.hackathonhub.serviceuser;

import com.hackathonhub.serviceuser.controllers.RoleController;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleController roleController;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createRole_TestValid() {
        /*
               GIVEN
         */

        Role roleForSave = new Role()
                .setId(UUID.randomUUID())
                .setRole_name(RoleEnum.ROLE_USER);



        /*
               MOCKS SETTING
         */

        when(roleRepository.save(any(Role.class))).thenReturn(roleForSave);

        /*
               EXECUTE
         */

        Role savedRole = roleController.createRole(roleForSave);

        /*
               VERIFY AFTER
         */

        verify(roleRepository).save(any(Role.class));

        /*
               ASSERTIONS
         */

        Assertions.assertEquals(roleForSave, savedRole);
    }

    @Test
    void createRole_TestInvalid() {
        /*
               GIVEN
         */

        Role roleForSave = null;

        /*
               ASSERTIONS
         */

        Assertions.assertThrows(
                NullPointerException.class,
                () -> roleController.createRole(roleForSave),
                "Cannot invoke \"com.hackathonhub.serviceuser.models.Role.getRole_name()" +
                        "\" because \"role\" is null"
        );
    }
}
