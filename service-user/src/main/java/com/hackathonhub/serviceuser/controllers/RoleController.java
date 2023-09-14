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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Create role")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "Role created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ApiAuthResponse<Role>> create(@RequestBody Role role) {
        ApiAuthResponse<Role> savedRole = roleService.create(role);

        return ResponseEntity
                .status(savedRole.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                 .body(savedRole);
    }

    @Operation(summary = "Get role by id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Role found"),
                    @ApiResponse(responseCode = "404", description = "Role not found",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiAuthResponse<Role>> getById(@PathVariable("id") UUID id) {
        ApiAuthResponse<Role> role = roleService.getById(id);

        return ResponseEntity
                .status(role.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(role);
    }

    @Operation(summary = "Update role")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Role updated"),
                    @ApiResponse(responseCode = "404", description = "Role not found",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PutMapping ResponseEntity<ApiAuthResponse<Role>> updateRole(@RequestBody Role role) {
        ApiAuthResponse<Role> response = roleService.update(role);

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @Operation(summary = "Delete role")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Role deleted"),
                    @ApiResponse(responseCode = "404", description = "Role not found",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @DeleteMapping
    public ResponseEntity<ApiAuthResponse<Role>> deleteRole(@RequestBody Role role) {
        ApiAuthResponse<Role> response = roleService.delete(role);

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


}
