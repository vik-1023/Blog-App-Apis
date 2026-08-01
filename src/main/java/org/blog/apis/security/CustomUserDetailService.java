package org.blog.apis.security;

import org.blog.apis.entities.User;
import org.blog.apis.repositories.UserRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepositories userRepositories;

    public CustomUserDetailService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepositories.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + username));
        return new CustomUserDetails(user);
    }
}
