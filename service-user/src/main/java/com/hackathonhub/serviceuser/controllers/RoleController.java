package com.hackathonhub.serviceuser.controllers;


import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Create role", description = "If the response is successful, the created role will be " +
            "returned in the response body (data: Role)")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "Role created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ApiAuthResponse<Role>> createRole(@RequestBody Role role) {
        ApiAuthResponse<Role> savedRole = roleService.createRole(role);

        return ResponseEntity
                .status(savedRole.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                 .body(savedRole);
    }
}
