package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.VerifyUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest){
        return  new ResponseEntity<>(authenticationService.register(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }

    @PutMapping("/verify")
    public ResponseEntity<String> verifyUser(
        @RequestBody @Valid VerifyUserRequest verifyUserRequest,
        HttpServletRequest httpServletRequest){
        String authHeader = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(authenticationService.verifyEmail(verifyUserRequest, authHeader));
    }

}
