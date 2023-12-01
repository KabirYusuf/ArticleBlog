package dev.levelupschool.backend.service.implementation;


import dev.levelupschool.backend.data.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.service.interfaces.FileStorageService;
import dev.levelupschool.backend.util.Converter;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @InjectMocks
    private LevelUpUserService levelUpUserService;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private FileStorageService fileStorageService;

    private ArticleRepository articleRepository;
    @Captor
    private ArgumentCaptor<MultipartFile> fileCaptor;
    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;
        @BeforeEach
        void setUp() {
            userRepository = Mockito.mock(UserRepository.class);
            passwordEncoder = Mockito.mock(PasswordEncoder.class);
            jwtService = Mockito.mock(JwtService.class);
            fileStorageService = Mockito.mock(FileStorageService.class);
            articleRepository = Mockito.mock(ArticleRepository.class);

            levelUpUserService = new LevelUpUserService(userRepository, passwordEncoder, jwtService, fileStorageService, articleRepository);
        }


    @Test
    void givenRegistrationRequest_whenUserRegistersWithAnImage_theUserSavedContainsWhatTheFileStorageServiceReturns() {
        String validBase64ImageString = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4" +
            "//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUser");
        registrationRequest.setPassword("testPassword");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFirstName("Test");
        registrationRequest.setLastName("User");
        registrationRequest.setUserImage(validBase64ImageString);


        when(userRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        try (MockedStatic<Converter> mockedConverter = Mockito.mockStatic(Converter.class)) {
            MultipartFile mockFile = Mockito.mock(MultipartFile.class);
            mockedConverter.when(() -> Converter.base64StringToMultipartFile(anyString(), anyString())).thenReturn(mockFile);

            when(fileStorageService.saveFile(any(MultipartFile.class), anyString())).thenReturn("fileUrl");

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            levelUpUserService.registerUser(registrationRequest);

            verify(fileStorageService).saveFile(fileCaptor.capture(), stringCaptor.capture());
            MultipartFile capturedFile = fileCaptor.getValue();
            String capturedString = stringCaptor.getValue();
            assertNotNull(capturedFile);
            assertEquals("blog-user-images", capturedString);

            verify(userRepository).save(userCaptor.capture());
            User capturedUser = userCaptor.getValue();

            assertNotNull(capturedUser);
            assertEquals("testUser", capturedUser.getUsername());
            assertEquals("test@example.com", capturedUser.getEmail());
            assertEquals("fileUrl", capturedUser.getUserImage());
        }
    }
}
