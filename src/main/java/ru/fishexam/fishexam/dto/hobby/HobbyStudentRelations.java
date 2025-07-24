package ru.fishexam.fishexam.dto.hobby;

public class HobbyStudentRelations {
    private Long hobbyStydentId;
    private Long hobbyId;
    private Long stydentId;

    public HobbyStudentRelations(Long hobbyId, Long stydentId) {
        this.hobbyId = hobbyId;
        this.stydentId = stydentId;
    }

    public Long getHobbyStydentId() {
        return hobbyStydentId;
    }

    public void setHobbyStydentId(Long hobbyStydentId) {
        this.hobbyStydentId = hobbyStydentId;
    }

    public Long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(Long hobbyId) {
        this.hobbyId = hobbyId;
    }

    public Long getStydentId() {
        return stydentId;
    }

    public void setStydentId(Long stydentId) {
        this.stydentId = stydentId;
    }
}
