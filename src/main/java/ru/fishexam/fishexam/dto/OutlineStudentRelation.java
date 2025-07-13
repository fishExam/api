package ru.fishexam.fishexam.dto;

public class OutlineStudentRelation {
    private Long outlineStudentId;
    private Long outlineId;
    private Long studentId;

    public Long getOutlineStudentId() {
        return outlineStudentId;
    }

    public void setOutlineStudentId(Long outlineStudentId) {
        this.outlineStudentId = outlineStudentId;
    }

    public Long getOutlineId() {
        return outlineId;
    }

    public void setOutlineId(Long outlineId) {
        this.outlineId = outlineId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
