package com.findmeapp.findme.Services.Authorization;

import com.findmeapp.findme.Models.DTO.JwtAuthenticationResponse;
import com.findmeapp.findme.Models.DTO.SignInUserDTO;
import com.findmeapp.findme.Models.DTO.SignUpUserDTO;
import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Models.Enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService service;
    @Mock
    private UserService userService;
    @Mock
    private JWTService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void signUpTest(){
        SignUpUserDTO request = new SignUpUserDTO();
        request.setUsername("testuser");
        request.setEmail("testuser@example.com");
        request.setPassword("password123");

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        String token = "mockJwtToken";
        Mockito.when(jwtService.generateToken(user)).thenReturn(token);

        JwtAuthenticationResponse response = service.signUp(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(token, response.getToken());

        verify(passwordEncoder).encode(request.getPassword());
        verify(userService).createUser(any(User.class));
        verify(jwtService).generateToken(user);

    }


    @Test
    void signInTest(){

    }

}
