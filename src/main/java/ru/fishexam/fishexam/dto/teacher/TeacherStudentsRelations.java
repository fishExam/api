package ru.fishexam.fishexam.dto.teacher;

public class TeacherStudentsRelations {
    private Long teacherStudentsId;
    private Long teacherId;
    private Long studentId;

    public Long getTeacherStudentsId() {
        return teacherStudentsId;
    }

    public void setTeacherStudentsId(Long teacherStudentsId) {
        this.teacherStudentsId = teacherStudentsId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
