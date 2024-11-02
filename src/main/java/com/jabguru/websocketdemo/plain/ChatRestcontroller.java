package com.jabguru.websocketdemo.plain;

import com.jabguru.websocketdemo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ChatRestcontroller {
    private final ChatMessageHandler chatMessageHandler;

    @PostMapping("/sendViaAPI")
    public ChatMessage saveEntity(@RequestBody ChatMessage message) throws IOException {
        System.out.println("It got to this place you");
        System.out.println(message);
        chatMessageHandler.sendMessage(message.getContent());
        return message;
    }
}
