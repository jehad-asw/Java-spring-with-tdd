package com.jehad.lion.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostRepository postRepository;

    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {

        posts = List.of(
                new Post(1, 1, "hello title 1", "this is the body of first record", 0),
                new Post(2, 2, "title 2", "this is body of second record", 0));

    }

    @Test
    void shouldFindAllPosts() throws Exception {

        String response = """
                [{
                       "userId": 1,
                       "id": 1,
                       "version": 0,
                       "title": "hello title 1",
                       "body": "this is the body of first record"
                     },
                     {
                       "userId": 2,
                       "id": 2,
                       "version": 0,
                       "title": "title 2",
                       "body": "this is body of second record"
                     }]
                """;

        when(postRepository.findAll()).thenReturn(posts);
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    //
    // /api/post/1
    @Test
    void shouldFindPostWhenFailedId() throws Exception {

        var json = """
                 {
                       "userId": 1,
                       "id": 1,
                       "version": 0,
                       "title": "hello title 1",
                       "body": "this is the body of first record"
                     }
                """;
        when(postRepository.findById(1)).thenReturn(Optional.of(posts.get(0)));
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
    // /api/post/999

    @Test
    void shouldNotFindPostWhenNotInvalidID() throws Exception {

        when(postRepository.findById(999)).thenThrow(ResponseNotFoundException.class);
        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    void shouldCreatePostWhenPostIsValid() throws Exception {

        var post = new Post(5,5,"save","save this record to repository",null);
        var json = """
                 {
                       "userId": 5,
                       "id": 5,
                       "version": 0,
                       "title": "save",
                       "body": "save this record to repository"
                     }
                """;


        when(postRepository.save(post)).thenReturn(post);

        mockMvc.perform(post("/api/posts")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldNotCreatePostWhenNotValid() throws Exception {
        var post = new Post(5,5,"","",null);
        var json = """
                 {
                       "userId": 5,
                       "id": 5,
                       "version": 0,
                       "title": "",
                       "body": ""
                     }
                """;

        when(postRepository.save(post)).thenReturn(post);

        mockMvc.perform(post("/api/posts")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePostWhenValidPost() throws Exception {
        Post post = new Post(1,1,"new title","this is new body",1);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        var json = """
                 {
                       "userId": 1,
                       "id": 1,
                       "version": 1,
                       "title": "new title",
                       "body": "this is new body"
                     }
                """;

        mockMvc.perform(put("/api/posts/1")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdatePostWhenNotValidPost() throws Exception {
        Post post = new Post(1,1,"new title","this is new body",1);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        var json = """
                 {
                       "userId": 1,
                       "id": 1,
                       "version": 1,
                       "title": "",
                       "body": ""
                     }
                """;

        mockMvc.perform(put("/api/posts/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeletePostWhenValidPost() throws Exception {

        doNothing().when(postRepository).deleteById(1);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());

        verify(postRepository,times(1)).deleteById(1);
    }
}
