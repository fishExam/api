package ru.fishexam.fishexam.auth.models;

import java.util.Collection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private final String username;

  private final Long userId;

  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(String username, Long userId, String password, Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.userId = userId;
    this.password = password;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }


  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
