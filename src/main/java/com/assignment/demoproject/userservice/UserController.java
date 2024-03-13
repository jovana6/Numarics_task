package com.assignment.demoproject.userservice;

import com.assignment.demoproject.productservice.AuthorizationFactory;
import jakarta.ws.rs.HeaderParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    private final JwtTokenProvider jwtTokenProvider;


    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Register a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "User registered",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("com.assignment.demoproject.userservice.User is registered", HttpStatus.CREATED);
    }

    @Operation(summary = "Login user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "User logged in",
                    name = "Response"
            ),
            @APIResponse(responseCode = "400",
                    description = "Bad Request",
                    name = "ErrorResponse"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken();
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer"));
    }
}
