package com.dongVu1105.AI_service.service;

import com.dongVu1105.AI_service.dto.request.ChatAgentRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class ChatAgentService {
    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    public ChatAgentService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();

        chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

//    public String chat (ChatAgentRequest request){
//        return chatClient.prompt(request.getMessage()).call().content();
//    }

    public String chat (ChatAgentRequest request){
        SystemMessage systemMessage = new SystemMessage(
                "You are PetStory AI support. kiki is just a pet of lili, not a person");
        UserMessage userMessage = new UserMessage(request.getMessage());
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient.prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(
                ChatMemory.CONVERSATION_ID, request.getConversationId()))
                .call().content();
    }

    public String chatWithMedia (MultipartFile file, String message){
        Media media = Media.builder().mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                .data(file.getResource())
                .build();
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0D)
                .build();

        return chatClient
                .prompt()
                .options(chatOptions)
                .system("You are PetStory AI support")
                .user(promptUserSpec -> promptUserSpec.media(media).text(message))
                .call()
                .content();
    }


}
