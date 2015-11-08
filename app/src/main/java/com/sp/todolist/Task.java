package com.sp.todolist;

/**
 * Created by Muhd on 8/7/2015.
 */
public class Task {

    private String id;
    private String title;
    private String content;

    Task (String taskId, String taskTitle, String taskContent) {
        id = taskId;
        title = taskTitle;
        content = taskContent;

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
