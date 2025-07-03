package ru.fishexam.fishexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dao.StudentDao;
import ru.fishexam.fishexam.dao.TeacherDao;
import ru.fishexam.fishexam.service.StudentService;
import ru.fishexam.fishexam.service.TeacherService;
import ru.fishexam.fishexam.service.UserProfileService;

@Configuration
public class CommonConfig {

    @Bean
    public TeacherDao teacherDao(PostgreSqlJdbcTemplate mainDb) {
        return new TeacherDao(mainDb);
    }

    @Bean
    public StudentDao studentDao(PostgreSqlJdbcTemplate mainDb) {
        return new StudentDao(mainDb);
    }

    @Bean
    public StudentService studentService(StudentDao studentDao) {
        return new StudentService(studentDao);
    }

    @Bean
    public TeacherService teacherService(TeacherDao teacherDao) {
        return new TeacherService(teacherDao);
    }

    @Bean
    public UserProfileService userProfileService(TeacherService teacherService, StudentService studentService) {
        return new UserProfileService(teacherService, studentService);
    }
}
