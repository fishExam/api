package ru.fishexam.fishexam.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.auth.dao.RefreshTokenDao;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.security.JwtUtils;
import ru.fishexam.fishexam.auth.services.AuthService;
import ru.fishexam.fishexam.auth.services.DataSecurityService;
import ru.fishexam.fishexam.auth.services.RefreshTokenService;
import ru.fishexam.fishexam.auth.services.UserDetailsServiceImpl;
import ru.fishexam.fishexam.service.UserProfileService;

@Configuration
public class CommonSecurityConfig {

    @Bean
    public RefreshTokenDao refreshTokenRepository(PostgreSqlJdbcTemplate postgreSqlJdbcTemplate) {
        return new RefreshTokenDao(postgreSqlJdbcTemplate);
    }

    @Bean
    public UserDao userRepository(PostgreSqlJdbcTemplate postgreSqlJdbcTemplate) {
        return new UserDao(postgreSqlJdbcTemplate);
    }

    @Bean
    public AuthService authService(
            AuthenticationManager authenticationManager,
            UserDao userDao,
            JwtUtils jwtUtils,
            RefreshTokenService refreshTokenService,
            PasswordEncoder passwordEncoder,
            UserProfileService userProfileService
    ) {
        return new AuthService(authenticationManager, userDao, jwtUtils, refreshTokenService, passwordEncoder, userProfileService);
    }

    @Bean
    public DataSecurityService dataSecurityService() {
        return new DataSecurityService();
    }

    @Bean
    public RefreshTokenService refreshTokenService(RefreshTokenDao refreshTokenDao) {
        return new RefreshTokenService(refreshTokenDao);
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(UserDao userDao) {
        return new UserDetailsServiceImpl(userDao);
    }

}
