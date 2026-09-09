package dev.hollandwesley.avarra.security;

import dev.hollandwesley.avarra.user.User;
import dev.hollandwesley.avarra.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads Avarra user accounts for Spring Security authentication.
 */
@Service
public class AvarraUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AvarraUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities("USER")
                .build();
    }
}