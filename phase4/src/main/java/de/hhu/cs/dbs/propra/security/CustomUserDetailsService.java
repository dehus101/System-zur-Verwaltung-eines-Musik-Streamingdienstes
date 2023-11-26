package de.hhu.cs.dbs.propra.security;

import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.repos.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> optionalUser = userRepository.findUser(username);
            if (optionalUser.isEmpty()) {
                String message = "User with unique string '" + username + "' does not exist.";
                throw new UsernameNotFoundException(message);
            }
            User user = optionalUser.get();
            return new UserToUserDetailsAdapter(user);
        } catch (UsernameNotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            String message = "User with unique string '" + username + "' cannot be found.";
            throw new UsernameNotFoundException(message, exception);
        }
    }
}
