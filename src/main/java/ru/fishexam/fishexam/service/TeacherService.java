package ru.fishexam.fishexam.service;

import org.apache.http.client.methods.HttpPost;
import org.springframework.transaction.annotation.Transactional;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dao.*;
import ru.fishexam.fishexam.dto.*;
import ru.fishexam.fishexam.gigachat.service.GigaChatAuthService;
import ru.fishexam.fishexam.gigachat.service.GigaChatService;
import ru.fishexam.fishexam.utils.CommonMappers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TeacherService {
    private final UserDao userDao;

    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final TeacherStudentsDao teacherStudentsDao;
    private final OutlineCreateDao outlineCreateDao;
    private final OutlineStudentRelationsDao outlineStudentRelationsDao;
    private final TaskModelDao taskModelDao;
    private final HomeworkModelDao homeworkModelDao;
    private final HomeworkTaskRelationsDao homeworkTaskRelationsDao;
    private final HomeworkUserRelationsDao homeworkUserRelationsDao;
    private final StudentAnswersDao studentAnswersDao;
    private final HobbyModelDao hobbyModelDao;
    private final GigaChatService gigaChatService;
    private final GigaChatAuthService gigaChatAuthService;

    public TeacherService(UserDao userDao, TeacherDao teacherDao, StudentDao studentDao, TeacherStudentsDao teacherStudentsDao, OutlineCreateDao outlineCreateDao, OutlineStudentRelationsDao outlineStudentRelationsDao, TaskModelDao taskModelDao, HomeworkModelDao homeworkModelDao, HomeworkTaskRelationsDao homeworkTaskRelationsDao, HomeworkUserRelationsDao homeworkUserRelationsDao, StudentAnswersDao studentAnswersDao, HobbyModelDao hobbyModelDao, GigaChatService gigaChatService, GigaChatAuthService gigaChatAuthService) {
        this.userDao = userDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.teacherStudentsDao = teacherStudentsDao;
        this.outlineCreateDao = outlineCreateDao;
        this.outlineStudentRelationsDao = outlineStudentRelationsDao;
        this.taskModelDao = taskModelDao;
        this.homeworkModelDao = homeworkModelDao;
        this.homeworkTaskRelationsDao = homeworkTaskRelationsDao;
        this.homeworkUserRelationsDao = homeworkUserRelationsDao;
        this.studentAnswersDao = studentAnswersDao;
        this.hobbyModelDao = hobbyModelDao;
        this.gigaChatService = gigaChatService;
        this.gigaChatAuthService = gigaChatAuthService;
    }

    public TeacherProfile createBaseTeacherProfile(Long userId, String username, String first_name, String patronymic,
                                                   String phone, String email, LocalDate birth, String telegram_id) {
        TeacherProfile teacherProfile = new TeacherProfile();
        teacherProfile.setTeacherId(userId);
        teacherProfile.setSurname(username);
        teacherProfile.setFirstName(first_name);
        teacherProfile.setPatronymic(patronymic);
        teacherProfile.setPhone(phone);
        teacherProfile.setEmail(email);
        teacherProfile.setBirth(birth);
        teacherProfile.setTelegramId(telegram_id);
        teacherDao.update(teacherProfile);
        return teacherProfile;
    }

    @Transactional(transactionManager = "mainDbTransactionManager", rollbackFor = Throwable.class)
    public TeacherProfile updateTeacherProfile(Long userId, TeacherProfileRequest teacherProfileRequest) {
        var oldProfile = getById(userId);

        UserAuth userAuth = userDao.findByUsername(oldProfile.getSurname()).orElseThrow();
        System.out.println(userAuth.getPassword()+"            15");
        UserAuth userProfile = new UserAuth(userId, teacherProfileRequest.surname(), teacherProfileRequest.firstName(),
                teacherProfileRequest.patronymic(), teacherProfileRequest.phone(), teacherProfileRequest.email(),
                teacherProfileRequest.birth(), teacherProfileRequest.telegramId(), userAuth.getPassword());
        userDao.update(userProfile);
        var newProfile = CommonMappers.mapFromRequestTeacherProfile(userId, teacherProfileRequest);
        var updateProfile = CommonMappers.mergeTwoTeacherProfile(oldProfile, newProfile);

        teacherDao.update(updateProfile);

        return updateProfile;
    }

    public TeacherProfile getById(Long userId) {
        return teacherDao.getById(userId).orElseThrow();
    }

    public TeacherStudentsRelations assignStudent(Long teacherId, Long studentId) {
        TeacherStudentsRelations teacherStudentsRelations = new TeacherStudentsRelations();
        teacherStudentsRelations.setTeacherId(teacherId);
        teacherStudentsRelations.setStudentId(studentId);
        teacherStudentsDao.update(teacherStudentsRelations);
        return teacherStudentsRelations;
    }

    public OutlineCreate createOutline(Long teacherId, OutlineCreateRequest outlineCreateRequest) {
        OutlineCreate outlineCreate = new OutlineCreate();
        outlineCreate.setAuthorId(teacherId);
        outlineCreate.setTitle(outlineCreateRequest.title());
        outlineCreateDao.update(outlineCreate);
        return outlineCreate;

    }

    public OutlineStudentRelation assignOutline(Long studentId, Long outlineId) {
        OutlineStudentRelation outlineStudentRelation = new OutlineStudentRelation();
        outlineStudentRelation.setStudentId(studentId);
        outlineStudentRelation.setOutlineId(outlineId);
        outlineStudentRelationsDao.update(outlineStudentRelation);
        return outlineStudentRelation;
    }

    public TaskModel createTask(Long teacherId, TaskModelRequest taskModelRequest) {
        TaskModel taskModel = new TaskModel();
        taskModel.setAuthorId(teacherId);
        taskModel.setTitle(taskModelRequest.title());
        taskModel.setAnswer(taskModelRequest.answer());
        taskModelDao.update(taskModel);
        return taskModel;
    }

    public HomeworkModel createHomework(Long teacherId, HomeworkModelRequest homeworkModelRequest) {
        HomeworkModel homeworkModel = new HomeworkModel();
        homeworkModel.setAuthorId(teacherId);
        homeworkModel.setDescription(homeworkModelRequest.description());
        homeworkModelDao.update(homeworkModel);
        String numbersStr = homeworkModelRequest.description(); // "1 2 4 55 7" - если описание такое
        String[] numbersAsString = numbersStr.split(" ");
        for (String taskIdStr : numbersAsString) {
            HomeworkTaskRelations homeworkTaskRelations = new HomeworkTaskRelations();
            homeworkTaskRelations.setTaskId(Long.parseLong(taskIdStr));
            homeworkTaskRelations.setHomeworkId(homeworkModelDao.getByInfo(teacherId, homeworkModelRequest.description()));
            homeworkTaskRelationsDao.update(homeworkTaskRelations);
        }
        return homeworkModel;
    }

    public HomeworkUserRelations assignHomework(Long studentId, Long homeworkId) {
        HomeworkUserRelations homeworkUserRealations = new HomeworkUserRelations();
        homeworkUserRealations.setStudentId(studentId);
        homeworkUserRealations.setHomeworkId(homeworkId);
        homeworkUserRelationsDao.update(homeworkUserRealations);
        return homeworkUserRealations;
    }

    public StudentAnswers checkHomework(Long studentAnswersId, StudentAnswersRequest studentAnswersRequest) {
        StudentAnswers studentAnswers = studentAnswersDao.getById(studentAnswersId).orElseThrow();
        TaskModel task = taskModelDao.getById(studentAnswers.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        boolean isCorrect = task.getAnswer().trim().equalsIgnoreCase(studentAnswersRequest.studentAnswer().trim());
        HomeworkUserRelations homeworkUserRelations = homeworkUserRelationsDao.getById(studentAnswers.getHomeworkUserId())
                .orElseThrow(() -> new RuntimeException("Homework not assigned to student"));
        List<HomeworkUserRelations> homeworkUser = homeworkUserRelationsDao.getByStudentId(homeworkUserRelations.getStudentId());
        for (HomeworkUserRelations i: homeworkUser) {
            studentAnswers.setHomeworkUserId(i.getHomeworkUserId());
            studentAnswers.setTaskId(task.getTaskId());
            studentAnswers.setStudentAnswer(studentAnswersRequest.studentAnswer());
            studentAnswers.setCorrect(isCorrect);
            studentAnswers.setFeedback(studentAnswersRequest.feedback());
            studentAnswersDao.update(studentAnswers);
        }
        return studentAnswers;
    }

    public List<StudentStatistics> getStudentsStatistics(Long teacherId) {
        List<Long> studentIds = teacherStudentsDao.getStudentsByTeacherId(teacherId);
        List<StudentStatistics> statistics = new ArrayList<>();
        for (Long studentId : studentIds) {
            StudentStatistics stat = new StudentStatistics();
            stat.setStudentId(studentId);
            StudentProfile student = studentDao.getById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            stat.setFirstName(student.getFirstName());
            stat.setSurname(student.getSurname());
            List<StudentAnswers> answers = studentAnswersDao.getAnswersByStudentId(studentId);
            int totalTasks = answers.size();
            int correctTasks = (int) answers.stream().filter(StudentAnswers::getIsCorrect).count();
            stat.setTotalTasks(totalTasks);
            stat.setCorrectTasks(correctTasks);
            stat.setIncorrectTasks(totalTasks - correctTasks);
            statistics.add(stat);
        }
        return statistics;
    }

    public TaskModel generateErrorCorrectionTask(Long teacherId, Long studentId) {
        return new TaskModel();
    }

    public TaskModel gamifyErrorCorrectionTask(Long studentId, Long taskId) {
        TaskModel task = taskModelDao.getById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        List<String> hobbies = hobbyModelDao.getStudentHobby(studentId);
        String hobby = hobbies.isEmpty() ? "none" : hobbies.get(new Random().nextInt(hobbies.size()));
        String gamifiedDescription = generateGamifiedDescription(task, hobby);
        TaskModel taskModel = new TaskModel();
        taskModel.setTitle(gamifiedDescription);
        taskModel.setAnswer(task.getAnswer());
        taskModel.setAuthorId(task.getAuthorId());
        taskModelDao.update(taskModel);
        return taskModel;
    }

    private String generateGamifiedDescription(TaskModel task, String hobby) {
        String prompt = String.format(
                "Создай игровое описание учебного задания, чтобы оно было интересным для ученика с хобби '%s'. " +
                        "Задание: '%s'. Правильный ответ: '%s'. " +
                        "Включи хобби учащегося в описание задания, чтобы сделать его увлекательным и понятным, " +
                        "используя повествование или контекст, связывающий задание с хобби. " +
                        "Верни только геймифицированное описание в виде одной строки, без дополнительных пояснений.",
                hobby, task.getTitle(), task.getAnswer()
        );

        try {
            return gigaChatService.sendMessage(prompt);
        } catch (IOException e) {
            return switch (hobby.toLowerCase()) {
                case "aviation" -> String.format("Представь, что ты пилот, решающий '%s', чтобы безопасно проложить маршрут! Правильный ответ: '%s'.", task.getTitle(), task.getAnswer());
                case "music" -> String.format("Составь решение для '%s', словно это музыкальная нота!", task.getTitle());
                default -> String.format("Реши увлекательную задачу '%s'!", task.getTitle());
            };
        }
    }
}