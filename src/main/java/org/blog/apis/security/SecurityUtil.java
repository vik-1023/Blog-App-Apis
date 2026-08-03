package org.blog.apis.security;

import org.blog.apis.entities.Post;
import org.blog.apis.entities.User;
import org.blog.apis.repositories.UserRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final UserRepositories userRepositories;

    public SecurityUtil(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getName();
        return userRepositories.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public boolean isCurrentAdmin(User user) {

        return user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    public boolean isOwner(User user, Post post) {

        return post.getUser().getId().equals(user.getId());
    }
}
