package com.project.taskipro.model.tasks.ai;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OpenAIResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice{
        private Message message;

        public Message getMessage() {
            return message;
        }

        @Getter
        @Setter
        public static class Message{
            private String content;

            public String getContent() {
                return content;
            }
        }
    }
}
