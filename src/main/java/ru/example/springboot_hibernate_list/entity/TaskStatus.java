package ru.example.springboot_hibernate_list.entity;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private final String view;

    private TaskStatus(String value) {
        this.view = value;
    }

    public String getView() {
        return this.view;
    }
}
