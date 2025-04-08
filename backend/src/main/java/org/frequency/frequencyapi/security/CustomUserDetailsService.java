package org.frequency.frequencyapi.security;

import org.frequency.frequencyapi.models.User;
import org.frequency.frequencyapi.mySQLRepositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

    public static String extractNameFromEmail(String emailString) {
        int index = emailString.indexOf('@');
        if (index == -1) {
            return emailString;
        }
        return emailString.substring(0, index);
    }

}
