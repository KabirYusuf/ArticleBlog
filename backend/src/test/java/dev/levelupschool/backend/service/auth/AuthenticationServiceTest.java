package dev.levelupschool.backend.service.auth;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.NotificationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.VerificationToken;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.service.interfaces.VerificationTokenService;
import dev.levelupschool.backend.service.notification.LevelUpEmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private LevelUpEmailSenderService levelUpEmailSenderService;
    @Autowired
    private UserRepository userRepository;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    private RegistrationRequest registrationRequest;


    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("kaybee");
        registrationRequest.setEmail("sonkaybee@gmail.com");
        registrationRequest.setPassword("1234567");

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("1234567");

        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    public void testThatWhenAUserRegistersUserRecordSizeIncreasesByOneInDB(){
        int numberOfUsersBeforeRegistrationOfFirstUser = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistrationOfFirstUser);
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
        authenticationService.register(registrationRequest);
        int numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterSecondUserTriesToUseSameEmailAsTheFirstUser);
        verify(levelUpEmailSenderService, times(1)).sendEmailNotification(any(NotificationRequest.class));
    }

    @Test
    public void testThatMoreThanOneUserCantUseTheSameUsernameToRegister(){
        int numberOfUsersBeforeRegistrationOfFirstUser = userRepository.findAll().size();
        assertEquals(0, numberOfUsersBeforeRegistrationOfFirstUser);
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
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
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
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
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        authenticationRequest.setUsername("invalidEmailUsername");


        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

    @Test
    void testThatWhenAUserTriesToLoginWithAnIncorrectPassword_ExceptionIsThrown(){
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        authenticationRequest.setPassword("InvalidPassword");

        assertThrows(UserException.class, ()-> authenticationService.login(authenticationRequest));
    }

}
