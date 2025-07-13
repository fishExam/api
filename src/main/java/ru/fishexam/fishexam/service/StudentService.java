package ru.fishexam.fishexam.service;

import org.springframework.transaction.annotation.Transactional;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dao.*;
import ru.fishexam.fishexam.dto.*;
import ru.fishexam.fishexam.utils.CommonMappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final UserDao userDao;

    private final StudentDao studentDao;
    private final HobbyModelDao hobbyModelDao;
    private final HobbyStudentRelationsDao hobbyStudentRelationsDao;
    private final HomeworkUserRelationsDao homeworkUserRelationsDao;
    private final HomeworkModelDao homeworkModelDao;
    private final TaskModelDao taskModelDao;
    private final StudentAnswersDao studentAnswersDao;

    public StudentService(UserDao userDao, StudentDao studentDao, HobbyModelDao hobbyModelDao, HobbyStudentRelationsDao hobbyStudentRelationsDao, HomeworkUserRelationsDao homeworkUserRelationsDao, HomeworkModelDao homeworkModelDao, TaskModelDao taskModelDao, StudentAnswersDao studentAnswersDao) {
        this.userDao = userDao;
        this.studentDao = studentDao;
        this.hobbyModelDao = hobbyModelDao;
        this.hobbyStudentRelationsDao = hobbyStudentRelationsDao;
        this.homeworkUserRelationsDao = homeworkUserRelationsDao;
        this.homeworkModelDao = homeworkModelDao;
        this.taskModelDao = taskModelDao;
        this.studentAnswersDao = studentAnswersDao;
    }

    public StudentProfile createBaseStudentProfile(Long userId, String username, String first_name, String patronymic,
                                                   String phone, String email, LocalDate birth, String telegram_id) {
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setStudentId(userId);
        studentProfile.setSurname(username);
        studentProfile.setFirstName(first_name);
        studentProfile.setPatronymic(patronymic);
        studentProfile.setPhone(phone);
        studentProfile.setEmail(email);
        studentProfile.setBirth(birth);
        studentProfile.setTelegramId(telegram_id);
        studentProfile.setTasksCount(0);
        studentDao.update(studentProfile);
        return studentProfile;
    }

    public StudentProfile updateStudentProfile(Long userId, StudentProfileRequest studentProfileRequest) {
        var oldProfile = getById(userId);

        UserAuth userAuth = userDao.findByUsername(oldProfile.getSurname()).orElseThrow();
        System.out.println(userAuth.getPassword()+"            15");
        UserAuth userProfile = new UserAuth(userId, studentProfileRequest.surname(), studentProfileRequest.firstName(),
                studentProfileRequest.patronymic(), studentProfileRequest.phone(), studentProfileRequest.email(),
                studentProfileRequest.birth(), studentProfileRequest.telegramId(), userAuth.getPassword());
        userDao.update(userProfile);
        var newProfile = CommonMappers.mapFromRequestStudentProfile(userId, studentProfileRequest);

        var updateProfile = CommonMappers.mergeTwoStudentProfile(oldProfile, newProfile);

        studentDao.update(updateProfile);

        return updateProfile;
    }

    public StudentProfile getById(Long userId) {
        return studentDao.getById(userId).orElseThrow();
    }

    public HobbyStudentRelations createHobby(Long userId, HobbyModelRequest hobbyModelRequest) {
        Optional<HobbyModel> optionalHobbyModel = hobbyModelDao.createIfNot(hobbyModelRequest.topic());
        if (optionalHobbyModel.isEmpty()){
            HobbyModel hobbyModel = new HobbyModel();
            hobbyModel.setTopic(hobbyModelRequest.topic());
            hobbyModelDao.update(hobbyModel);
        }
        HobbyStudentRelations hobbyStudentRelations = new HobbyStudentRelations();
        hobbyStudentRelations.setHobbyId(hobbyModelDao.getId(hobbyModelRequest.topic()));
        hobbyStudentRelations.setStydentId(userId);
        hobbyStudentRelationsDao.update(hobbyStudentRelations);
        return hobbyStudentRelations;
    }

    public List<HomeworkModel> getAssignedHomeworks(Long userId) {
        List<HomeworkUserRelations> relations = homeworkUserRelationsDao.getByStudentId(userId);
        List<HomeworkModel> responses = new ArrayList<>();
        for (HomeworkUserRelations relation : relations) {
            HomeworkModel homework = homeworkModelDao.getById(relation.getHomeworkId())
                    .orElseThrow(() -> new RuntimeException("Homework not found: " + relation.getHomeworkId()));
            List<String> tasks = new ArrayList<>();
            if (homework.getDescription() != null && !homework.getDescription().isBlank()) {
                String[] taskIds = homework.getDescription().split("\\s+");
                for (String taskIdStr : taskIds) {
                    try {
                        Long taskId = Long.parseLong(taskIdStr);
                        TaskModel task = taskModelDao.getById(taskId)
                                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
                        tasks.add(task.getTitle());
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid task ID in homework description: " + taskIdStr);
                    }
                }
            }
            homework.setDescription(homework.getDescription()+ " \n " + String.join(" \n ", tasks));
            responses.add(homework);
        }
        return responses;
    }

    public StudentProfile getStudentStatistics(Long userId) {
        StudentProfile student = getById(userId);
        List<StudentAnswers> answers = studentAnswersDao.getAnswersByStudentId(userId);
        int totalTasks = answers.size();
        int correctTasks = (int) answers.stream().filter(StudentAnswers::getIsCorrect).count();
        student.setTasksCount(correctTasks);
        return student;
    }

    public StudentAnswers submitAnswer(Long userId, Long homeworkId, Long taskId, StudentAnswersRequest answerRequest) {
        TaskModel task = taskModelDao.getById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        boolean isCorrect = task.getAnswer().trim().equalsIgnoreCase(answerRequest.studentAnswer().trim());
        StudentAnswers studentAnswer = new StudentAnswers();
        studentAnswer.setHomeworkUserId(homeworkUserRelationsDao.getByStudentIdAndHomeworkId(userId, homeworkId));
        studentAnswer.setTaskId(taskId);
        studentAnswer.setStudentAnswer(answerRequest.studentAnswer());
        studentAnswer.setCorrect(isCorrect);
        studentAnswer.setFeedback(answerRequest.feedback());
        studentAnswersDao.update(studentAnswer);
        return studentAnswer;
    }
}
