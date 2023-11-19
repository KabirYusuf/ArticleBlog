package dev.levelupschool.backend.service.implementation;
import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.model.User;
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
    private AuthenticationRequest authenticationRequest;
    @BeforeEach
    void setUp(){
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("kabir@gmail.com");
        authenticationRequest.setPassword("1234");
    }

    @Test
    public void givenIHaveAuthors_whenIFindAnAuthorWithId_theAuthorWithThatIdIsReturned(){
        userService.registerUser(authenticationRequest);
        User foundUser = userService.findUserById(1L);
        assertNotNull(foundUser);
    }
    @Test
    public void givenIHaveAuthors_whenIDeleteAnAuthor_theTableSizeOfAuthorDecreasesByOne(){
        userService.registerUser(authenticationRequest);
        int numberOfAuthorsBeforeDeletingAnAuthor = userService.findAllUsers().size();
        assertEquals(1, numberOfAuthorsBeforeDeletingAnAuthor);

        userService.deleteUser(1L);

        int numberOfAuthorsAfterDeletingAnAuthor = userService.findAllUsers().size();
        assertEquals(0, numberOfAuthorsAfterDeletingAnAuthor);
    }
    @Test
    public void givenIHaveAnAuthor_whenIUpdateTheAuthor_theUpdateAuthorIsReturned(){
        userService.registerUser(authenticationRequest);
        String nameOfAuthorBeforeUpdatingAuthor = userService.findUserById(1L).getFirstName();
        assertNull(nameOfAuthorBeforeUpdatingAuthor);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLastName("papez");

        User updatedUser = userService.updateUser(updateUserRequest, 1L);
        assertEquals("Papez", updatedUser.getLastName());
    }

}
