package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.model.Role;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.CommunicationException;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.service.interfaces.FileStorageService;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.util.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class LevelUpUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final FileStorageService fileStorageService;
    public LevelUpUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        FileStorageService fileStorageService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        User foundUser = findUserByUsername(registrationRequest.getUsername());
        if (foundUser != null)throw new UserException("User with "+ registrationRequest.getUsername()+ " already exist");
        User newUser = new User();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.USER);
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setRoles(roleSet);
        newUser.setEmail(registrationRequest.getEmail());
        String firstName = covertNameFirstCharacterToUpperCase(registrationRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(registrationRequest.getLastName());
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        processFileStorage(registrationRequest.getUserImage(), registrationRequest.getUsername(), newUser);
        return userRepository.save(newUser);
    }

    private void processFileStorage(String image, String name, User user) {
        if (image != null){
            MultipartFile file = Converter.base64StringToMultipartFile(image, name);
            String fileUrl = null;
            try {
                fileUrl = fileStorageService.saveFile(file, "blog-user-images").get();
            } catch (InterruptedException | ExecutionException e) {
                throw new CommunicationException(e.getMessage());
            }
            user.setUserImage(fileUrl);
        }
    }

    private String covertNameFirstCharacterToUpperCase(String name){
        if (name != null) return name.substring(0,1).toUpperCase() + name.substring(1);
        return null;
    }

    @Override
    public User findUserById(Long authorId) {
        return userRepository
            .findById(authorId)
            .orElseThrow(()-> new ModelNotFoundException(authorId));
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository
            .findAll(pageable);
    }

    @Override
    public void deleteUser(Long userId, String authHeader) {
        User foundUser = getUser(authHeader);
        if (!Objects.equals(foundUser.getId(), userId))throw new UserException("You have no permission to delete user");
        userRepository
            .deleteById(userId);
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long userId, String authHeader) {
        User foundUser = getUser(authHeader);
        if (!Objects.equals(foundUser.getId(), userId))throw new UserException("You have no permission to update user");
        String firstName = covertNameFirstCharacterToUpperCase(updateUserRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(updateUserRequest.getLastName());
        foundUser.setFirstName(firstName);
        foundUser.setLastName(lastName);
        processFileStorage(updateUserRequest.getUserImage(), foundUser.getUsername(), foundUser);
        return userRepository.save(foundUser);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository
            .findByUsernameIgnoreCase(username)
            .orElse(null);
    }

    @Override
    public User getUser(String authHeader) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepository.findUsersByUsernameIgnoreCase(email)
            .orElseThrow(()-> new UserException("User not found"));
    }
}
