package dev.levelupschool.backend.service.auth;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("kaybee");
        registrationRequest.setEmail("k@gmail.com");
        registrationRequest.setPassword("1234567");

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("1234567");

        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    public void testThatWhenAUserRegistersUserRecordSizeIncreasesByOneInDB(){
        int numberOfUsersBeforeRegistration = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistration);

        authenticationService.register(registrationRequest);

        int numberOfUsersAfterRegistration = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterRegistration);
    }

    @Test
    public void testThatMoreThanOneUserCantUseTheSameUsernameToRegister(){
        int numberOfUsersBeforeRegistrationOfFirstUser = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistrationOfFirstUser);

        authenticationService.register(registrationRequest);

        int numberOfUsersAfterRegistrationFirstUser = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterRegistrationFirstUser);

        RegistrationRequest registrationRequestForAnotherUser = new RegistrationRequest();
        registrationRequestForAnotherUser.setUsername("kaybee");
        registrationRequestForAnotherUser.setEmail("k@gmail.com");
        registrationRequestForAnotherUser.setPassword("1234567");

        assertThrows(UserException.class, ()-> authenticationService.register(registrationRequestForAnotherUser));

        int numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser);
    }
    @Test
    public void testThatWhenAUserLogsInWithValidCredentials_JwtIsReturned(){
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        authenticationResponse = authenticationService.login(authenticationRequest);

        assertNotNull(authenticationResponse);

        log.info("Authentication Response:: "+ authenticationResponse.getToken());
    }

    @Test
    void testThatWhenAUserTriesToLoginWithAnIncorrectUsername_ExceptionIsThrown(){
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        authenticationRequest.setUsername("invalidEmailUsername");


        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

    @Test
    void testThatWhenAUserTriesToLoginWithAnIncorrectPassword_ExceptionIsThrown(){
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        authenticationRequest.setPassword("InvalidPassword");

        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

}
