package com.project.taskipro.model.tasks.ai;

import java.util.List;

public class OpenAIRequest {
    private String model = "gpt-3.5-turbo";
    private List<Message> messages;

    public String getModel() {
        return model;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public OpenAIRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public OpenAIRequest() {}

    public static class Message{
        private String role;
        private String content;

        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Message(String role, String content){
            this.role = role;
            this.content = content;
        }

        public Message(){}
    }
}
