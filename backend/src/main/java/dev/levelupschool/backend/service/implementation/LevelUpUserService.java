package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.model.Role;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class LevelUpUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    public LevelUpUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User registerUser(AuthenticationRequest authenticationRequest) {
        User foundUser = findUserByEmail(authenticationRequest.getEmail());
        if (foundUser != null)throw new UserException("User with "+ authenticationRequest.getEmail()+ " already exist");
        User newUser = new User();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.USER);
        newUser.setEmail(authenticationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));
        newUser.setRoles(roleSet);
        return userRepository.save(newUser);
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
    public void deleteUser(Long authorId) {
        userRepository
            .deleteById(authorId);
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long authorId) {
        User foundUser = findUserById(authorId);
        String firstName = covertNameFirstCharacterToUpperCase(updateUserRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(updateUserRequest.getLastName());
        foundUser.setFirstName(firstName);
        foundUser.setLastName(lastName);
        return userRepository.save(foundUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository
            .findByEmailIgnoreCase(email)
            .orElse(null);
    }

    @Override
    public User getUser(String authHeader) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepository.findUsersByEmailIgnoreCase(email)
            .orElseThrow(()-> new UserException("User not found"));
    }
}
