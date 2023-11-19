package dev.levelupschool.backend.service.auth;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
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

    @BeforeEach
    void setUp(){
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("kabir@gmail.com");
        authenticationRequest.setPassword("1234567");

        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    public void testThatWhenAUserRegistersUserRecordSizeIncreasesByOneInDB(){
        int numberOfUsersBeforeRegistration = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistration);

        authenticationService.register(authenticationRequest);

        int numberOfUsersAfterRegistration = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterRegistration);
    }

    @Test
    public void testThatMoreThanOneUserCantUseTheSameEmailToRegister(){
        int numberOfUsersBeforeRegistrationOfFirstUser = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistrationOfFirstUser);

        authenticationService.register(authenticationRequest);

        int numberOfUsersAfterRegistrationFirstUser = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterRegistrationFirstUser);

        AuthenticationRequest authenticationRequestOfSecondUser = new AuthenticationRequest();
        authenticationRequestOfSecondUser.setEmail("kabir@gmail.com");
        authenticationRequestOfSecondUser.setPassword("2345");

        assertThrows(UserException.class, ()-> authenticationService.register(authenticationRequestOfSecondUser));

        int numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser);
    }
    @Test
    public void testThatWhenAUserLogsInWithValidCredentials_JwtIsReturned(){
        authenticationService.register(authenticationRequest);

        authenticationResponse = authenticationService.login(authenticationRequest);

        assertNotNull(authenticationResponse);

        log.info("Authentication Response:: "+ authenticationResponse.getToken());
    }

    @Test
    void testThatWhenAUserTriesToLoginWithAnIncorrectEmail_ExceptionIsThrown(){
        authenticationService.register(authenticationRequest);

        authenticationRequest.setEmail("invalidEmail@gmail.com");

        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

    @Test
    void testThatWhenAUserTriesToLoginWithAnIncorrectPassword_ExceptionIsThrown(){
        authenticationService.register(authenticationRequest);

        authenticationRequest.setPassword("InvalidPassword");

        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

}
