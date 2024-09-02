package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Models.DTO.JwtAuthenticationResponse;
import com.findmeapp.findme.Models.DTO.SignInUserDTO;
import com.findmeapp.findme.Models.DTO.SignUpUserDTO;
import com.findmeapp.findme.Services.Authorization.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpUserDTO request) {

        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInUserDTO request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
