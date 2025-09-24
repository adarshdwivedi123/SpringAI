package com.teleusko.SpringAICode.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private VectorStore vectorStore;

    @PostConstruct
    public void  initData(){
        TextReader textReader = new TextReader(new ClassPathResource("product_details.txt"));
        TokenTextSplitter  tokenTextSplitter = new TokenTextSplitter(100,30,5,500,false);
        List<Document> documents =tokenTextSplitter.split(textReader.get());
        vectorStore.add(documents);
    }
}
