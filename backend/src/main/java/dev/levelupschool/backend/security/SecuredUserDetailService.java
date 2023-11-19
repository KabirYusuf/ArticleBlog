package dev.levelupschool.backend.security;

import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.UserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecuredUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    public SecuredUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findUsersByEmailIgnoreCase(username)
            .map(SecuredUser::new)
            .orElseThrow(()-> new UserException("Username/Password is incorrect"));
    }
}
