package ru.fishexam.fishexam.auth.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.auth.models.UserDetailsImpl;
import ru.fishexam.fishexam.auth.dao.UserDao;

public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserDao userDao;

  public UserDetailsServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAuth user = userDao.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return new UserDetailsImpl(
            user.getUsername(),
            user.getUserId(),
            user.getPassword(),
            List.of()
    );
  }

}
