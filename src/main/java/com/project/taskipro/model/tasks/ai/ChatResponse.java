package com.project.taskipro.model.tasks.ai;

public class ChatResponse {
    private String response;
    private String status;
    private String error;

    // Геттеры и сеттеры
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
