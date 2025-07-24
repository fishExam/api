package ru.fishexam.fishexam.dto.hobby;

public class HobbyModel {
    private Long hobbyId;
    private String topic;

    public HobbyModel(Long hobbyId, String topic) {
        this.hobbyId = hobbyId;
        this.topic = topic;
    }

    public Long getHobbyId() {
        return hobbyId;
    }

    public void setHobbyId(Long hobbyId) {
        this.hobbyId = hobbyId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
