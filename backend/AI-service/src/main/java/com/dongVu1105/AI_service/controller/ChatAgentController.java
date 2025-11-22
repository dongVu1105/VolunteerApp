package com.dongVu1105.AI_service.controller;

import com.dongVu1105.AI_service.dto.ApiResponse;
import com.dongVu1105.AI_service.dto.request.ChatAgentRequest;
import com.dongVu1105.AI_service.service.ChatAgentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatAgentController {
    ChatAgentService chatAgentService;

    @PostMapping("/only-text")
    public ApiResponse<String> chatOnlyText (@RequestBody ChatAgentRequest request){
        return ApiResponse.<String>builder().result(chatAgentService.chat(request)).build();
    }

    @PostMapping("/with-media")
    public ApiResponse<String> chatWithMedia (@RequestParam MultipartFile file, @RequestParam String message){
        return ApiResponse.<String>builder().result(chatAgentService.chatWithMedia(file, message)).build();
    }
}
