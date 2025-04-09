package com.project.taskipro.service;

import com.project.taskipro.model.tasks.ai.ChatResponse;
import com.project.taskipro.model.tasks.ai.OpenAIRequest;
import com.project.taskipro.model.tasks.ai.OpenAIResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ChatApiService {
    private final WebClient webClient;
    public static final String FORMAT_STRING = "Оцени время решения и сложость задачи: %s\nСтек технологий: %s\nСписок пользователей: %s\n%s";
    private static final String PROMPT = "Дай ответ в следующем виде: Количество дней (рекомендуемое количество дней на выполнение задачи), Пользователь (имя пользователя из приведенных)";

    public ChatApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getMessageFromChatGPT(long deskId, long taskId, String description, String stack, String users) {
        String message = String.format(FORMAT_STRING, description, stack, users, PROMPT);
        Mono<ChatResponse> responseMono = sendMessage(message, deskId, taskId);
        try {
            return responseMono.map(ChatResponse::getResponse).toFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private Mono<ChatResponse> sendMessage(String request, long deskId, long taskId) {
        OpenAIRequest openAIRequest = createRequest(request);
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(openAIRequest)
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .map(this::convertToChatResponse)
                .onErrorResume(e -> Mono.just(createErrorResponse(e.getMessage())));
    }

    private OpenAIRequest createRequest(String request) {
        OpenAIRequest openAIRequest = new OpenAIRequest();
        openAIRequest.setMessages(List.of(
                new OpenAIRequest.Message("system", "Ответь на русском языке"),
                new OpenAIRequest.Message("user", request)
        ));
        return openAIRequest;
    }

    private ChatResponse createErrorResponse(String error) {
        ChatResponse response = new ChatResponse();
        response.setStatus("error");
        response.setError(error != null ? error : "Unknown error");
        return response;
    }

    private ChatResponse convertToChatResponse(OpenAIResponse openAIResponse) {
        ChatResponse result = new ChatResponse();
        if (openAIResponse.getChoices()!=null && !openAIResponse.getChoices().isEmpty()){
            result.setStatus("success");
            result.setResponse(openAIResponse.getChoices().get(0).getMessage().getContent());
        }else{
            result = createErrorResponse("Empty response");
        }
        return result;
    }
}
