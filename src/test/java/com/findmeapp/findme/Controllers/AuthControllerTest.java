package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Models.DTO.JwtAuthenticationResponse;
import com.findmeapp.findme.Models.DTO.SignInUserDTO;
import com.findmeapp.findme.Models.DTO.SignUpUserDTO;
import com.findmeapp.findme.Services.Authorization.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController controller;

    @Mock
    private AuthService authenticationService;

    @Test
    void signUpTest() {
        SignUpUserDTO request = new SignUpUserDTO();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("testuser@example.com");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("mockJwtToken");

        Mockito.when(authenticationService.signUp(any(SignUpUserDTO.class))).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = controller.signUp(request);

        Assertions.assertEquals(response, result.getBody());
        Assertions.assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void signInTest() {
        SignInUserDTO request = new SignInUserDTO();
        request.setUsername("testuser");
        request.setPassword("password123");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("mockJwtToken");

        Mockito.when(authenticationService.signIn(any(SignInUserDTO.class))).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = controller.signIn(request);

        Assertions.assertEquals(response, result.getBody());
        Assertions.assertEquals(200, result.getStatusCode().value());
    }
}
