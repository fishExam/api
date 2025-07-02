package ru.fishexam.fishexam.auth.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.fishexam.fishexam.auth.security.AuthEntryPointJwt;
import ru.fishexam.fishexam.auth.security.AuthTokenFilter;
import ru.fishexam.fishexam.auth.security.JwtUtils;
import ru.fishexam.fishexam.auth.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig  {

  @Bean
  public JwtUtils jwtUtils() {
    return new JwtUtils("sdfsdfdsfsdfsdfdsfdsfsdfdsfsfsdfsfsdfsdfsdfsdfsdfsdfsdfsdfsfsd", TimeUnit.DAYS.toMillis(1));
  }

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
    return new AuthTokenFilter(jwtUtils, userDetailsService);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(
          UserDetailsServiceImpl userDetailsService
  ) {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  
  @Bean
  public SecurityFilterChain filterChain(
          HttpSecurity http,
          AuthEntryPointJwt unauthorizedHandler,
          DaoAuthenticationProvider authenticationProvider,
          AuthTokenFilter authenticationJwtTokenFilter
  ) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> 
          auth
                  .requestMatchers("/api/auth/**").permitAll()
                  .requestMatchers("/error").permitAll()
                  .anyRequest().authenticated()
        );
    
    http.authenticationProvider(authenticationProvider);

    http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

  @Bean
  public AuthEntryPointJwt authEntryPointJwt() {
    return new AuthEntryPointJwt();
  }
}
