package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpUserService implements UserService {
    private final UserRepository userRepository;
    public LevelUpUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        User newUser = new User();
        String firstName = covertNameFirstCharacterToUpperCase(createUserRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(createUserRequest.getLastName());
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(createUserRequest.getEmail());
        User savedUser = userRepository.save(newUser);
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setName(savedUser.getFirstName() + " " + savedUser.getLastName());
        createUserResponse.setId(savedUser.getId());
        return createUserResponse;
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
    public List<User> findAllUsers() {
        return userRepository
            .findAll();
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
}
