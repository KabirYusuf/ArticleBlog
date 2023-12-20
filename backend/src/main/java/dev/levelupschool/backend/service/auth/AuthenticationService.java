package dev.levelupschool.backend.service.auth;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.NotificationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.VerifyUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.VerificationToken;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.exception.UserNotVerifiedException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import dev.levelupschool.backend.service.interfaces.VerificationTokenService;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.service.notification.LevelUpEmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;

    private final VerificationTokenService verificationTokenService;

    private final LevelUpEmailSenderService levelUpEmailSenderService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
        JwtService jwtService,
        UserService userService,
        VerificationTokenService verificationTokenService,
        AuthenticationManager authenticationManager,
        LevelUpEmailSenderService levelUpEmailSenderService,
        PasswordEncoder passwordEncoder
    ){
        this.jwtService = jwtService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.authenticationManager = authenticationManager;
        this.levelUpEmailSenderService = levelUpEmailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(RegistrationRequest registrationRequest){
        User newUser = userService.registerUser(registrationRequest);

        VerificationToken verificationToken = verificationTokenService.createUniqueVerificationToken(newUser.getId());

        sendVerificationToken(registrationRequest.getEmail(), verificationToken.getToken());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        String token = jwtService.generateToken(new SecuredUser(newUser));

        authenticationResponse.setToken(token);

        authenticationResponse.setMessage("Verification email has been sent to your mail");
        return authenticationResponse;
    }
//    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
//        try{
//            authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(),
//                    authenticationRequest.getPassword()
//                )
//            );
//        }catch (InternalAuthenticationServiceException | BadCredentialsException internalAuthenticationServiceException){
//            throw new UserException("Username/Password is incorrect");
//        }
//
//        User foundUser = userService.findUserByUsername(authenticationRequest.getUsername());
//
//        String jwtToken = jwtService.generateToken(new SecuredUser(foundUser));
//
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//        authenticationResponse.setToken(jwtToken);
//        authenticationResponse.setMessage("Login Successful");
//        return authenticationResponse;
//    }

//    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
//
//        try {
//            authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(),
//                    authenticationRequest.getPassword()
//                )
//            );
//        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
//            throw new UserException("Username/Password is incorrect");
//        }
//
//
//        User foundUser = userService.findUserByUsername(authenticationRequest.getUsername());
//
//
//        String jwtToken = jwtService.generateToken(new SecuredUser(foundUser));
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//        authenticationResponse.setToken(jwtToken);
//
//
//        if (!foundUser.isVerified()) {
//
//            VerificationToken verificationToken = verificationTokenService.createUniqueVerificationToken(foundUser.getId());
//            sendVerificationToken(foundUser.getEmail(), verificationToken.getToken());
//
//
//            authenticationResponse.setMessage("Verification is pending. A new verification token has been sent to your email.");
//        } else {
//
//            authenticationResponse.setMessage("Login Successful");
//        }
//
//        return authenticationResponse;
//    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User foundUser = userService.findUserByUsername(authenticationRequest.getUsername());


        if (foundUser == null || !passwordEncoder.matches(authenticationRequest.getPassword(), foundUser.getPassword())) {
            throw new UserException("Username/Password is incorrect");
        }


        if (!foundUser.isVerified()) {
            VerificationToken verificationToken = verificationTokenService.createUniqueVerificationToken(foundUser.getId());
            sendVerificationToken(foundUser.getEmail(), verificationToken.getToken());
            throw new UserNotVerifiedException("Verification is pending. A new verification token has been sent to your email.");
        }


        String jwtToken = jwtService.generateToken(new SecuredUser(foundUser));
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setMessage("Login Successful");
        return authenticationResponse;
    }


    private void sendVerificationToken(String email, String verificationToken){
        Context context = new Context();
        context.setVariable("verificationToken", verificationToken);
        context.setVariable("currentYear", LocalDate.now().getYear());
        NotificationRequest notificationRequest = new NotificationRequest();

        notificationRequest.setTo(email);
        notificationRequest.setSubject("LevelUp Verification Email");
        notificationRequest.setSender("LevelUp");
        notificationRequest.setContent("verificationEmail");
        notificationRequest.setContext(context);

        levelUpEmailSenderService.sendEmailNotification(notificationRequest);
    }
//    @Transactional
//    public String verifyEmail(VerifyUserRequest verifyUserRequest, String authHeader){
//        User foundUser = userService.getUser(authHeader);
//        VerificationToken foundToken = verificationTokenService.findByToken(verifyUserRequest.getVerificationToken());
//
//        if (foundToken == null || foundToken.getExpiredAt().isBefore(LocalDateTime.now()))throw new UserException("Invalid token provided");
//
//        if (!Objects.equals(foundUser.getId(), foundToken.getUserId()))throw new UserException("Invalid token");
//
//        foundUser.setVerified(true);
//
//        verificationTokenService.deleteToken(foundToken.getId());
//
//        return "Account Successfully Verified";
//
//    }

    @Transactional
    public AuthenticationResponse verifyEmail(VerifyUserRequest verifyUserRequest) {
//        User foundUser = userService.getUser(authHeader);
        VerificationToken foundToken = verificationTokenService.findByToken(verifyUserRequest.getVerificationToken());

        if (foundToken == null || foundToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new UserException("Invalid token provided");
        }

        User foundUser = userService.findUserById(foundToken.getUserId());



//        if (!Objects.equals(foundUser.getId(), foundToken.getUserId())) {
//            throw new UserException("Invalid token");
//        }

        foundUser.setVerified(true);
        verificationTokenService.deleteToken(foundToken.getId());


        String jwtToken = jwtService.generateToken(new SecuredUser(foundUser));


        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setMessage("Account successfully verified and authenticated");

        return authenticationResponse;
    }


}
