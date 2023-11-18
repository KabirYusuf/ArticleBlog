package dev.levelupschool.backend.controller;
import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createAuthor(@RequestBody AuthenticationRequest authenticationRequest){
        return new ResponseEntity<>(userService.registerUser(authenticationRequest), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllAuthors(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getAuthorById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateAuthor(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable("id") Long id){
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
}
