package ru.fishexam.fishexam.dto;

public class OutlineCreate {
    private Long outlineId;
    private Long authorId;
    private String title;

    public Long getOutlineId() {
        return outlineId;
    }

    public void setOutlineId(Long outline_id) {
        this.outlineId = outline_id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long author_id) {
        this.authorId = author_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
