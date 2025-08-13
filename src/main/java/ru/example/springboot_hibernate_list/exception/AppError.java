package ru.example.springboot_hibernate_list.exception;

public class AppError {
    private int status;
    private String error;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;
    }

    public AppError() {
    }

    public AppError(int status, String message) {
        this.status = status;
        this.error = message;
    }
}
