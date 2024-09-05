package com.findmeapp.findme.Services.Authorization;

import com.findmeapp.findme.Models.DTO.JwtAuthenticationResponse;
import com.findmeapp.findme.Models.DTO.SignUpUserDTO;
import com.findmeapp.findme.Models.DTO.SignInUserDTO;
import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Models.Enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * User registration
     *
     * @param request User data
     * @return token
     */
    public JwtAuthenticationResponse signUp(SignUpUserDTO request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        user = userService.createUser(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * User authentication
     *
     * @param request User data
     * @return token
     */
    public JwtAuthenticationResponse signIn(SignInUserDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
