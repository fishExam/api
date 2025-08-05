package ru.fishexam.fishexam.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.dao.*;
import ru.fishexam.fishexam.dao.hobby.HobbyModelDao;
import ru.fishexam.fishexam.dao.hobby.HobbyStudentRelationsDao;
import ru.fishexam.fishexam.dao.homework.HomeworkModelDao;
import ru.fishexam.fishexam.dao.homework.HomeworkTaskRelationsDao;
import ru.fishexam.fishexam.dao.homework.HomeworkUserRelationsDao;
import ru.fishexam.fishexam.dao.outline.OutlineCreateDao;
import ru.fishexam.fishexam.dao.outline.OutlineStudentRelationsDao;
import ru.fishexam.fishexam.dao.student.StudentAnswersDao;
import ru.fishexam.fishexam.dao.student.StudentDao;
import ru.fishexam.fishexam.dao.task.TaskModelDao;
import ru.fishexam.fishexam.dao.teacher.TeacherDao;
import ru.fishexam.fishexam.dao.teacher.TeacherStudentsDao;
import ru.fishexam.fishexam.gigachat.service.GigaChatAuthService;
import ru.fishexam.fishexam.gigachat.service.GigaChatService;
import ru.fishexam.fishexam.service.StudentService;
import ru.fishexam.fishexam.service.TeacherService;
import ru.fishexam.fishexam.service.UserProfileService;

@Configuration
public class CommonConfig {

    @Value("${gigachat.client.id}")
    private String clientId;

    @Value("${gigachat.client.secret}")
    private String clientSecret;

    @Value("${gigachat.auth.url}")
    private String authUrl;

    @Value("${gigachat.api.url}")
    private String apiUrl;

    @Bean
    public ObjectMapper mainObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TeacherDao teacherDao(PostgreSqlJdbcTemplate mainDb) {
        return new TeacherDao(mainDb);
    }

    @Bean
    public StudentDao studentDao(PostgreSqlJdbcTemplate mainDb) {
        return new StudentDao(mainDb);
    }

    @Bean
    public HobbyModelDao hobbyModelDao(PostgreSqlJdbcTemplate mainDb) {
        return new HobbyModelDao(mainDb);
    }

    @Bean
    public HobbyStudentRelationsDao hobbyStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        return new HobbyStudentRelationsDao(mainDb);
    }

    @Bean
    public HomeworkUserRelationsDao homeworkUserRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        return new HomeworkUserRelationsDao(mainDb);
    }

    @Bean
    public HomeworkModelDao homeworkModelDao(PostgreSqlJdbcTemplate mainDb) {
        return new HomeworkModelDao(mainDb);
    }

    @Bean
    public TaskModelDao taskModelDao(PostgreSqlJdbcTemplate mainDb) {
        return new TaskModelDao(mainDb);
    }

    @Bean
    public StudentAnswersDao studentAnswersDao(PostgreSqlJdbcTemplate mainDb) {
        return new StudentAnswersDao(mainDb);
    }

    @Bean
    public TeacherStudentsDao teacherStudentsDao(PostgreSqlJdbcTemplate mainDb) {
        return new TeacherStudentsDao(mainDb);
    }

    @Bean
    public OutlineCreateDao outlineCreateDao(PostgreSqlJdbcTemplate mainDb) {
        return new OutlineCreateDao(mainDb);
    }

    @Bean
    public OutlineStudentRelationsDao outlineStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        return new OutlineStudentRelationsDao(mainDb);
    }

    @Bean
    public HomeworkTaskRelationsDao homeworkTaskRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        return new HomeworkTaskRelationsDao(mainDb);
    }
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public GigaChatAuthService gigaChatAuthService(CloseableHttpClient httpClient,  ObjectMapper mainObjectMapper) {
        return new GigaChatAuthService(httpClient, mainObjectMapper);
    }

    @Bean
    public GigaChatService gigaChatService(
            CloseableHttpClient httpClient,
            GigaChatAuthService gigaChatAuthService,
            ObjectMapper mainObjectMapper
    ) {
        return new GigaChatService(httpClient, gigaChatAuthService, mainObjectMapper, apiUrl);
    }

    @Bean
    public StudentService studentService(
            UserDao userDao,
            StudentDao studentDao,
            HobbyModelDao hobbyModelDao,
            HobbyStudentRelationsDao hobbyStudentRelationsDao,
            HomeworkUserRelationsDao homeworkUserRelationsDao,
            HomeworkModelDao homeworkModelDao,
            TaskModelDao taskModelDao,
            StudentAnswersDao studentAnswersDao
    ) {
        return new StudentService(userDao, studentDao, hobbyModelDao, hobbyStudentRelationsDao, homeworkUserRelationsDao, homeworkModelDao, taskModelDao, studentAnswersDao);
    }

    @Bean
    public TeacherService teacherService(
            UserDao userDao,
            TeacherDao teacherDao,
            StudentDao studentDao,
            TeacherStudentsDao teacherStudentsDao,
            OutlineCreateDao outlineCreateDao,
            OutlineStudentRelationsDao outlineStudentRelationsDao,
            TaskModelDao taskModelDao,
            HomeworkModelDao homeworkModelDao,
            HomeworkTaskRelationsDao homeworkTaskRelationsDao,
            HomeworkUserRelationsDao homeworkUserRelationsDao,
            StudentAnswersDao studentAnswersDao,
            HobbyModelDao hobbyModelDao,
            GigaChatService gigaChatService,
            GigaChatAuthService gigaChatAuthService
    ) {
        return new TeacherService(userDao, teacherDao, studentDao, teacherStudentsDao, outlineCreateDao, outlineStudentRelationsDao, taskModelDao, homeworkModelDao, homeworkTaskRelationsDao, homeworkUserRelationsDao, studentAnswersDao, hobbyModelDao, gigaChatService, gigaChatAuthService);
    }

    @Bean
    public UserProfileService userProfileService(TeacherService teacherService, StudentService studentService) {
        return new UserProfileService(teacherService, studentService);
    }
}
