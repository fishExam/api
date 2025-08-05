package ru.fishexam.fishexam.service;

import org.springframework.transaction.annotation.Transactional;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dao.hobby.HobbyModelDao;
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
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkModelRequest;
import ru.fishexam.fishexam.dto.homework.HomeworkUserRelations;
import ru.fishexam.fishexam.dto.outline.OutlineCreate;
import ru.fishexam.fishexam.dto.outline.OutlineCreateRequest;
import ru.fishexam.fishexam.dto.student.StudentAnswers;
import ru.fishexam.fishexam.dto.student.StudentAnswersRequest;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.student.StudentStatistics;
import ru.fishexam.fishexam.dto.task.TaskModel;
import ru.fishexam.fishexam.dto.task.TaskModelRequest;
import ru.fishexam.fishexam.dto.teacher.TeacherProfile;
import ru.fishexam.fishexam.dto.teacher.TeacherProfileRequest;
import ru.fishexam.fishexam.dto.teacher.TeacherStudentsRelations;
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

    public TeacherProfile createBaseTeacherProfile(
            String username,
            String first_name,
            String patronymic,
            String phone,
            String email,
            LocalDate birth,
            String telegram_id
    ) {
        TeacherProfile teacherProfile = new TeacherProfile(
                null,
                username,
                first_name,
                patronymic,
                phone,
                email,
                birth,
                telegram_id
        );
        return teacherDao.save(teacherProfile);
    }

    @Transactional(transactionManager = "mainDbTransactionManager", rollbackFor = Throwable.class)
    public TeacherProfile updateTeacherProfile(Long userId, TeacherProfileRequest teacherProfileRequest) {
        var oldProfile = getById(userId);

        if (!oldProfile.getUsername().equals(teacherProfileRequest.username())) {
            UserAuth userAuth = userDao.findById(userId).orElseThrow();

            userAuth.setUsername(teacherProfileRequest.username());

            userDao.update(userAuth);
        }

        var newProfile = CommonMappers.mapFromRequestTeacherProfile(userId, teacherProfileRequest);
        var updateProfile = CommonMappers.mergeTwoTeacherProfile(oldProfile, newProfile);

        teacherDao.update(updateProfile);

        return updateProfile;
    }

    public TeacherProfile getById(Long userId) {
        return teacherDao.getById(userId).orElseThrow();
    }

    public List<StudentProfile> assignStudent(Long teacherId, Long studentId) {
        if (teacherStudentsDao.existsByBothIds(studentId, teacherId)) {
            teacherStudentsDao.findStudentsByTeacherId(teacherId);
        }

        TeacherStudentsRelations teacherStudentsRelations = new TeacherStudentsRelations();
        teacherStudentsRelations.setTeacherId(teacherId);
        teacherStudentsRelations.setStudentId(studentId);
        return teacherStudentsDao.save(teacherStudentsRelations);
    }

    public OutlineCreate createOutline(Long teacherId, OutlineCreateRequest outlineCreateRequest) {
        var found = outlineCreateDao.findByAuthorIdAndTitle(teacherId, outlineCreateRequest.title());
        return found.orElseGet(() -> outlineCreateDao.save(teacherId, outlineCreateRequest.title()).orElseThrow());
    }

    public OutlineCreate assignOutline(Long studentId, Long outlineId) {
        if (outlineStudentRelationsDao.existsByOutlineStudent(studentId, outlineId))
            throw new RuntimeException("This outline was already attached to the student");
        OutlineCreate outlineCreate = outlineStudentRelationsDao.save(studentId, outlineId);
        return outlineCreate;
    }

    public TaskModel createTask(Long teacherId, TaskModelRequest taskModelRequest) {
        if (taskModelDao.existsByTaskTeacher(teacherId, taskModelRequest.title()))
            throw  new RuntimeException("This title was used in another task");
        TaskModel taskModel = taskModelDao.save(teacherId,
                taskModelRequest.title(),
                taskModelRequest.answer());
        return taskModel;
    }

    public HomeworkModel createHomework(Long teacherId, HomeworkModelRequest homeworkModelRequest) {
        if (homeworkModelDao.existsByHomework(teacherId, homeworkModelRequest.description()))
            throw new RuntimeException("This description was already attached");
        if (homeworkModelDao.existsTaskByHomework(homeworkModelRequest.description()))
            throw new RuntimeException("Task in homework not found");
        HomeworkModel homeworkModel = homeworkModelDao.save(teacherId,
                homeworkModelRequest.description());
        String numbersStr = homeworkModelRequest.description(); // "1 2 4 55 7" - если описание такое
        String[] numbersAsString = numbersStr.split(" ");
        for (String taskIdStr : numbersAsString)
            homeworkTaskRelationsDao.save(Long.parseLong(taskIdStr), homeworkModel.getHomeworkId());
        return homeworkModel;
    }

    public HomeworkModel assignHomework(Long studentId, Long homeworkId) {
        if (homeworkUserRelationsDao.existsByRelation(studentId, homeworkId))
            throw new RuntimeException("Student have this homework");
        HomeworkModel homeworkModel = homeworkUserRelationsDao.save(studentId, homeworkId).orElseThrow();
        String[] tasks = homeworkModel.getDescription().split(" ");
        for (String task : tasks){
            studentAnswersDao.save(homeworkUserRelationsDao.getByStudentIdAndHomeworkId(studentId, homeworkId),
                    (long) Integer.parseInt(task),
                    null, false);
        }
        return homeworkModel;
    }

    public StudentAnswers checkHomework(Long studentAnswersId, StudentAnswersRequest studentAnswersRequest) {
        StudentAnswers studentAnswers = studentAnswersDao.getById(studentAnswersId).orElseThrow(()-> new RuntimeException("Task not found"));
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
            stat.setSurname(student.getUsername());
            List<StudentAnswers> answers = studentAnswersDao.getAnswersByStudentId(studentId);
            int totalTasks = (int) answers.stream().filter(a -> a.getStudentAnswer()!=null).count();
            int correctTasks = (int) answers.stream().filter(StudentAnswers::getIsCorrect).count();
            stat.setTotalTasks(totalTasks);
            stat.setCorrectTasks(correctTasks);
            stat.setIncorrectTasks(totalTasks - correctTasks);
            statistics.add(stat);
        }
        return statistics;
    }

    public TaskModel gamifyErrorCorrectionTask(Long studentId, Long taskId) {
        TaskModel task = taskModelDao.getById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        List<String> hobbies = hobbyModelDao.getStudentHobby(studentId);
        String hobby = hobbies.isEmpty() ? "none" : hobbies.get(new Random().nextInt(hobbies.size()));
        String gamifiedDescription = generateGamifiedDescription(task, hobby);
        TaskModel taskModel = new TaskModel(task.getTaskId(), task.getAuthorId(),
                gamifiedDescription,
                task.getAnswer());
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
        } catch (Exception e) {
            return switch (hobby.toLowerCase()) {
                case "aviation" -> String.format("Представь, что ты пилот, решающий '%s', чтобы безопасно проложить маршрут! Правильный ответ: '%s'.", task.getTitle(), task.getAnswer());
                case "music" -> String.format("Составь решение для '%s', словно это музыкальная нота!", task.getTitle());
                default -> String.format("Реши увлекательную задачу '%s'!", task.getTitle());
            };
        }
    }

    public TaskModel generateErrorCorrectionTask(Long teacherId, Long studentId) {
        return null;
    }
}