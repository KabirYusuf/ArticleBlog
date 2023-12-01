package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.interfaces.UserService;
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
class LevelUpUserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private String authHeader;

    private RegistrationRequest registrationRequest;
    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("kabir@gmail.com");
        registrationRequest.setUsername("kaybee");
        registrationRequest.setPassword("12345");

        AuthenticationResponse authenticationResponse = authenticationService.register(registrationRequest);

        authHeader = "Bearer " + authenticationResponse.getToken();

    }

    @Test
    public void givenIHaveAuthors_whenIFindAnAuthorWithId_theAuthorWithThatIdIsReturned(){
        User foundUser = userService.findUserById(1L);
        assertNotNull(foundUser);
    }
    @Test
    public void givenIHaveAuthors_whenIDeleteAnAuthor_theTableSizeOfAuthorDecreasesByOne(){

        int numberOfAuthorsBeforeDeletingAnAuthor = userRepository.findAll().size();
        assertEquals(1, numberOfAuthorsBeforeDeletingAnAuthor);

        userService.deleteUser(1L, authHeader);

        int numberOfAuthorsAfterDeletingAnAuthor = userRepository.findAll().size();
        assertEquals(0, numberOfAuthorsAfterDeletingAnAuthor);
    }
    @Test
    public void givenIHaveAnAuthor_whenIUpdateTheAuthor_theUpdateAuthorIsReturned(){

        String nameOfAuthorBeforeUpdatingAuthor = userService.findUserById(1L).getFirstName();
        assertNull(nameOfAuthorBeforeUpdatingAuthor);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLastName("papez");

        User updatedUser = userService.updateUser(updateUserRequest, 1L, authHeader);
        assertEquals("Papez", updatedUser.getLastName());
    }

    @Test
    void givenUserAAndUserB_whenUserAFollowsUserB_userBGetsAdditionalFollowerAndUserASizeOfUsersFollowedIncreasesByOne(){
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        authenticationService.register(userBRequest);

        User userABeforeFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBBeforeUserAFollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingBeforeFollowingUserB = userABeforeFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB = userBBeforeUserAFollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingBeforeFollowingUserB);

        userService.followUser(userAAuthHeader, 3L);

        User userAAfterFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterFollowedByUserA = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterFollowingUserB = userAAfterFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB = userBAfterFollowedByUserA.getFollowers().size();

        assertEquals(1, numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB);
        assertEquals(1, numberOfUsersUserAIsFollowingAfterFollowingUserB);
    }

    @Test
    void givenUserAFollowsUserB_whenUserAUnfollowsUserB_userBNumberOfFollowersDecreasesByOneAndUserASizeOfUsersFollowedDecreasesByOne(){
        RegistrationRequest userARequest = new RegistrationRequest();
        userARequest.setUsername("kaybee1");
        userARequest.setEmail("sonkaybee@gmail.com");
        userARequest.setPassword("1234");
        AuthenticationResponse authenticationResponse = authenticationService.register(userARequest);

        String userAAuthHeader = "Bearer " + authenticationResponse.getToken();

        RegistrationRequest userBRequest = new RegistrationRequest();
        userBRequest.setUsername("kaybee2");
        userBRequest.setEmail("sonkaybee@gmail.com");
        userBRequest.setPassword("1234");

        authenticationService.register(userBRequest);

        User userABeforeFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBBeforeUserAFollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingBeforeFollowingUserB = userABeforeFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB = userBBeforeUserAFollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBBeforeUserAFollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingBeforeFollowingUserB);

        userService.followUser(userAAuthHeader, 3L);

        User userAAfterFollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterFollowedByUserA = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterFollowingUserB = userAAfterFollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB = userBAfterFollowedByUserA.getFollowers().size();

        assertEquals(1, numberOfUsersThatIsFollowingUserBAfterUserAFollowsUserB);
        assertEquals(1, numberOfUsersUserAIsFollowingAfterFollowingUserB);

        userService.unfollowUser(userAAuthHeader, 3L);

        User userAAfterUnfollowingUserB = userRepository.findByIdWithFollowing(2L).get();
        User userBAfterUserAUnfollowed = userRepository.findByIdWithFollowers(3L).get();

        int numberOfUsersUserAIsFollowingAfterUnfollowingUserB = userAAfterUnfollowingUserB.getFollowing().size();
        int numberOfUsersThatIsFollowingUserBAfterUserAUnfollowsUserB = userBAfterUserAUnfollowed.getFollowers().size();

        assertEquals(0, numberOfUsersThatIsFollowingUserBAfterUserAUnfollowsUserB);
        assertEquals(0, numberOfUsersUserAIsFollowingAfterUnfollowingUserB);
    }

}
