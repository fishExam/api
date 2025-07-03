package ru.fishexam.fishexam.auth.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.fishexam.fishexam.auth.models.UserDetailsImpl;

public class DataSecurityService {
    public boolean isOwner(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return userId.equals(userDetails.getUserId());
    }
}
