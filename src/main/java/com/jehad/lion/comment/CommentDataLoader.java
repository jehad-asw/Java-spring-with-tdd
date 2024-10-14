package com.jehad.lion.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jehad.lion.post.PostDataLoader;
import com.jehad.lion.post.PostRepository;
import com.jehad.lion.post.Posts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Component
public class CommentDataLoader implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CommentDataLoader.class);
    private final ObjectMapper objectMapper;
    private final CommentRepository commentRepository;

    public CommentDataLoader(ObjectMapper objectMapper, CommentRepository commentRepository) {
        this.objectMapper = objectMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if(commentRepository.count() ==0){
            String COMMENT_JSON= "/data/comments.json";
            log.info("Loading comments into database from JSON");
            try(InputStream inputStream = TypeReference.class.getResourceAsStream(COMMENT_JSON)){
                Comment[] response = objectMapper.readValue(inputStream,Comment[].class);
                commentRepository.saveAll(Arrays.stream(response).toList());
            }catch(IOException e){
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }

    }
}
