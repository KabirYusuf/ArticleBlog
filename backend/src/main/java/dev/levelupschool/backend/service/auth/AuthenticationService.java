package dev.levelupschool.backend.service.auth;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.Role;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import dev.levelupschool.backend.service.interfaces.TokenService;
import dev.levelupschool.backend.service.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        JwtService jwtService,
        UserService userService,
        TokenService tokenService,
        AuthenticationManager authenticationManager
    ){
        this.jwtService = jwtService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(AuthenticationRequest authenticationRequest){
        User newUser = userService.registerUser(authenticationRequest);
        String jwtToken = jwtService.generateToken(new SecuredUser(newUser));
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
                )
            );
        }catch (BadCredentialsException badCredentialsException){
            throw new UserException("Username/Password is incorrect");
        }

        User foundUser = userService.findUserByEmail(authenticationRequest.getEmail());

        String jwtToken = jwtService.generateToken(new SecuredUser(foundUser));

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }
}
