package com.teleusko.SpringAICode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class OpenAIController {
//using openAIChatModel
//    @Autowired
//    private OpenAiChatModel chatModel;
//
//    public OpenAIController(OpenAiChatModel chatModel){
//        this.chatModel =chatModel;
//    }
//    @GetMapping("/api/{message}")
//    public   String getAnswer(@PathVariable String message){
//        String response=chatModel.call(message);
//        return  "Hello World" +response;
//    }
  public  ChatClient chatClient;



    // Constructor injection (recommended instead of setting in method)
    public OpenAIController(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.
                create(chatModel);
    }

//    public OpenAIController(ChatClient.Builder builder) {
//        this.chatClient = builder
//                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemoryRepository()))
//                .build();
//    }

//    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message) {
//        ChatResponse chatResponse = (ChatResponse) chatClient
//                .prompt(message)
//                .call();
//
//        System.out.println(chatResponse.getMetadata().getModel());
//
//        String response = chatResponse.getResult().getOutput().getText();
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String  type , @RequestParam String year , @RequestParam String lang ){

        String  tempt = """
                        I want to watch a {type}  movie with  good rating,
                        looking for movie around  this year {year},
                        The language is looking for is {lang}
                        Suggest one specific  movie and tell me the cast and length of the movie.
                        """;
        PromptTemplate promptTemplate = new PromptTemplate(tempt);
        Prompt prompt = promptTemplate.create(Map.of("type",type,"year",year,"lang",lang));

        String response =chatClient
                .prompt(prompt)
                .call()
                .content();
        return  response;
    }




}
