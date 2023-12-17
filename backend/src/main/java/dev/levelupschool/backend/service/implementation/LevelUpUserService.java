package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.PaymentDetails;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.ArticleDTO;
import dev.levelupschool.backend.data.dto.response.UserDTO;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.GeoException;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.service.interfaces.FileStorageService;
import dev.levelupschool.backend.service.interfaces.GeoService;
import dev.levelupschool.backend.service.interfaces.PaymentService;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.util.Converter;
import dev.levelupschool.backend.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LevelUpUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final FileStorageService fileStorageService;
    private ArticleRepository articleRepository;
    private final PaymentService paymentService;
    private final GeoService geoService;
    public LevelUpUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        FileStorageService fileStorageService,
        ArticleRepository articleRepository,
        PaymentService paymentService,
        GeoService geoService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.fileStorageService = fileStorageService;
        this.articleRepository = articleRepository;
        this.paymentService = paymentService;
        this.geoService = geoService;
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
        if (image != null && !image.isEmpty()){
            MultipartFile file = Converter.base64StringToMultipartFile(image, name);
            String fileUrl = fileStorageService.saveFile(file, "blog-user-images");
            user.setUserImage(fileUrl);
        }
    }

    private String covertNameFirstCharacterToUpperCase(String name){
        if (name != null && !name.isEmpty()) return name.substring(0,1).toUpperCase() + name.substring(1);
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
        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()){
            foundUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
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
        String username = jwtService.extractUsername(jwt);
        return userRepository.findUsersByUsernameIgnoreCase(username)
            .orElseThrow(()-> new UserException("User not found"));
    }
    @Override
    @Transactional
    public void followUser(String authHeader, Long followedUserId) {
        User userFollowing = getUser(authHeader);

        User userFollowed = userRepository.findById(followedUserId)
            .orElseThrow(()-> new UserException("User not found"));

        if (!userFollowing.getFollowing().contains(userFollowed)) {
            userFollowing.getFollowing().add(userFollowed);
            userFollowed.getFollowers().add(userFollowing);

            userRepository.save(userFollowed);
            userRepository.save(userFollowing);
        } else {
            throw new UserException("You are already following this user");
        }
    }

    @Override
    @Transactional
    public void unfollowUser(String authHeader, Long followedUserId) {
        User userFollowing = getUser(authHeader);

        User userFollowed = userRepository.findById(followedUserId)
            .orElseThrow(()-> new UserException("User not found"));


        if (userFollowing.getFollowing().contains(userFollowed)) {
            userFollowing.getFollowing().remove(userFollowed);
            userFollowed.getFollowers().remove(userFollowing);

            userRepository.save(userFollowed);
            userRepository.save(userFollowing);
        } else {
            throw new UserException("You cannot unfollow a user you are not following");
        }
    }


    @Override
    public List<UserDTO> getFollowers(String authHeader) {
        User foundUser = getUser(authHeader);
        return foundUser
            .getFollowers()
            .stream()
            .map(UserDTO::new).toList();
    }

    @Override
    public List<UserDTO> getUsersFollowed(String authHeader) {
        User foundUser = getUser(authHeader);
        return foundUser
            .getFollowing()
            .stream()
            .map(UserDTO::new).toList();
    }

    @Override
    @Transactional
    public void bookmarkArticle(Long articleId, String authHeader) {
        User user = getUser(authHeader);
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new ModelNotFoundException(articleId));

        if (user.getBookmarkedArticles().contains(article)) {
            throw new UserException("Article already bookmarked");
        }

        user.getBookmarkedArticles().add(article);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unBookmarkArticle(Long articleId, String authHeader) {
        User user = getUser(authHeader);
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new ModelNotFoundException(articleId));

        if (!user.getBookmarkedArticles().contains(article)) {
            throw new UserException("Article not bookmarked");
        }

        user.getBookmarkedArticles().remove(article);
        userRepository.save(user);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ArticleDTO> getBookmarkedArticles(String authHeader) {
        User user = getUser(authHeader);

        return user.getBookmarkedArticles()
            .stream()
            .map(ArticleDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public String becomePremium(PaymentDetails paymentDetails, HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        User user = getUser(authHeader);
        String ipAddress = HttpUtil.getClientIpAddress(httpServletRequest);
        String country = geoService.getLocationInfo(ipAddress);

        if (geoService.isCountryBanned(country)){
            throw new GeoException("Access from your country is not allowed", HttpStatus.FORBIDDEN);
        }
        if (paymentService.processPayment(paymentDetails)) {
            user.setPremium(true);
            userRepository.save(user);
            return"Payment successful and premium status updated.";
        } else {
            return "Payment was not successful. Please try again.";
        }
    }
}
