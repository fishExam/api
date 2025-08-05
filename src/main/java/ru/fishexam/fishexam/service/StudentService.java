package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dao.hobby.HobbyModelDao;
import ru.fishexam.fishexam.dao.hobby.HobbyStudentRelationsDao;
import ru.fishexam.fishexam.dao.homework.HomeworkModelDao;
import ru.fishexam.fishexam.dao.homework.HomeworkUserRelationsDao;
import ru.fishexam.fishexam.dao.student.StudentAnswersDao;
import ru.fishexam.fishexam.dao.student.StudentDao;
import ru.fishexam.fishexam.dao.task.TaskModelDao;
import ru.fishexam.fishexam.dto.hobby.HobbyModel;
import ru.fishexam.fishexam.dto.hobby.HobbyModelRequest;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkUserRelations;
import ru.fishexam.fishexam.dto.student.StudentAnswers;
import ru.fishexam.fishexam.dto.student.StudentAnswersRequest;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.student.StudentProfileRequest;
import ru.fishexam.fishexam.dto.task.TaskModel;
import ru.fishexam.fishexam.utils.CommonMappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public StudentProfile createBaseStudentProfile(
            String username,
            String firstName,
            String patronymic,
            String phone,
            String email,
            LocalDate birth,
            String telegramId
    ) {
        StudentProfile studentProfile = new StudentProfile(
                null,
                username,
                firstName,
                patronymic,
                phone,
                email,
                birth,
                telegramId,
                null,
                0
        );
        return studentDao.save(studentProfile);
    }

    public StudentProfile updateStudentProfile(Long userId, StudentProfileRequest studentProfileRequest) {
        var oldProfile = getById(userId);

        if (!oldProfile.getUsername().equals(studentProfileRequest.username())) {
            UserAuth userAuth = userDao.findById(userId).orElseThrow();

            userAuth.setUsername(studentProfileRequest.username());

            userDao.update(userAuth);
        }

        var newProfile = CommonMappers.mapFromRequestStudentProfile(userId, studentProfileRequest);
        var updateProfile = CommonMappers.mergeTwoStudentProfile(oldProfile, newProfile);

        studentDao.update(updateProfile);

        return updateProfile;
    }

    public StudentProfile getById(Long userId) {
        return studentDao.getById(userId).orElseThrow();
    }

    public List<HobbyModel> createHobby(Long userId, HobbyModelRequest hobbyModelRequest) {
        hobbyModelDao.save(hobbyModelRequest.topic());
        if (hobbyStudentRelationsDao.existsByUsername(userId, hobbyModelRequest.topic()))
            throw new RuntimeException("Error: This student's hobby has already been created!");
        List<HobbyModel> hobList = hobbyStudentRelationsDao.save(hobbyModelDao.getId(hobbyModelRequest.topic()),
                userId);
        return hobList;
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
            homework.setDescription(homework.getDescription() + " \n " + String.join(" \n ", tasks));
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
