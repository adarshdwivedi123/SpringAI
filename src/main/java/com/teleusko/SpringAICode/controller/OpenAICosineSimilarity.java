package com.teleusko.SpringAICode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OpenAICosineSimilarity {

    public ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmbeddingModel embeddingModel;


    // Constructor injection (recommended instead of setting in method)
    public OpenAICosineSimilarity(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.
                create(chatModel);
    }


    @PostMapping("/apis/recommend")
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
    //Embedding
//    http://localhost:8080/api/embedding?text=Bottle
    @PostMapping("/apis/embedding")
    public float[] embeddings(@RequestParam String text){
        return  embeddingModel.embed(text);
    }

    @PostMapping("/apis/similarity")
    public  double  getSimilarity(@RequestParam String text1 ,@RequestParam String text2){
//        http://localhost:8080/apis/similarity?text1=Dog&text2=Cat
        // Generate embeddings using Spring AI
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        // Compute cosine similarity
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        //two word check for similartiy
        for (int i = 0; i < embedding1.length; i++) {
            dot += embedding1[i] * embedding2[i];
            normA += embedding1[i] * embedding1[i];
            normB += embedding2[i] * embedding2[i];
        }

        double similarity = dot / (Math.sqrt(normA) * Math.sqrt(normB));

        return similarity; // value between -1 and 1
    }

    @PostMapping("/apis/product")
    public List<Document> getProduct(@RequestParam String text) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());

    }
}
