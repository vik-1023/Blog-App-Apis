package org.blog.apis.controller;

import org.blog.apis.payloads.JwtAuthRequest;
import org.blog.apis.payloads.JwtAuthResponse;
import org.blog.apis.security.CustomUserDetailService;
import org.blog.apis.security.JwtHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final CustomUserDetailService customUserDetailService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtHelper jwtHelper,
                          CustomUserDetailService customUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.customUserDetailService = customUserDetailService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {

        // 1. Email aur password verify karega
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Database se user load karega
        UserDetails userDetails =
                customUserDetailService.loadUserByUsername(request.getEmail());

        // 3. JWT Token generate karega
        String token = jwtHelper.generateToken(userDetails);

        // 4. Response banayega
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);

        // 5. Token client ko return karega
        return ResponseEntity.ok(response);
    }
}
