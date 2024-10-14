package com.jehad.lion.comment;


import com.jehad.lion.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentRepository repository;
    List<Comment> commentList = new ArrayList<>();
    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void init() {
        posts = List.of(
                new Post(1, 1, "hello title 1", "this is the body of first record", 0),
                new Post(2, 2, "title 2", "this is body of second record", 0));

        commentList = List.of(
                new Comment(1, 1, "name-1", "email@hotmail.com", "body comment", 0),
                new Comment(2, 2, "name-2", "email2@hotmail.com", "second body comment", 0)
        );
    }

    @Test
    void shouldReturnAllComments() throws Exception {
        String response = """
                [{
                      "postId": 1,
                      "id": 1,
                      "version": 0,
                      "name": "name-1",
                      "email": "email@hotmail.com",
                      "body": "body comment"
                    },
                    {
                      "postId": 2,
                      "id": 2,
                      "version": 0,
                      "name": "name-2",
                      "email": "email2@hotmail.com",
                      "body": "second body comment"
                    }]
                """;
        when(repository.findAll()).thenReturn(commentList);

        mockMvc.perform(get("/api/comments/"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void shouldReturnCommentWhenValidId() throws Exception {
        String response = """
                {
                      "postId": 1,
                      "id": 1,
                      "version": 0,
                      "name": "name-1",
                      "email": "email@hotmail.com",
                      "body": "body comment"
                    }
                """;
        when(repository.findById(1)).thenReturn(Optional.ofNullable(commentList.get(0)));
        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void shouldNotReturnCommentWhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/comments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateCommentWhenValid() throws Exception {
        String comment = """
                {
                      "postId": 1,
                      "id": 1,
                      "version": 0,
                      "name": "name-1",
                      "email": "email@hotmail.com",
                      "body": "body comment"
                    }
                """;
        mockMvc.perform(post("/api/comments/")
                        .contentType("application/json")
                        .content(comment))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreatedCommentWhenInvalid() throws Exception {
        var comment = new Comment(1, 1, "", "", "body", 0);
        String response = """
                {
                      "postId": 1,
                      "id": 1,
                      "version": 0,
                      "name": "",
                      "email": "",
                      "body": "body comment"
                    }
                """;
        when(repository.save(comment)).thenReturn(comment);
        mockMvc.perform(post("/api/comments/")
                        .contentType("application/json")
                        .content(response))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatedWhenCommentValid() throws Exception {

        var comment = new Comment(1,1,"name","email@gmail.com","body comment",5);
        String json = """
                {
                      "postId": 1,
                      "id": 1,
                      "version": 5,
                      "name": "name",
                      "email": "email@gmail.com",
                      "body": "body comment"
                    }
                """;
        when(repository.save(comment)).thenReturn(comment);
        when(repository.findById(1)).thenReturn(Optional.of(comment));
        mockMvc.perform(put("/api/comments/1")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdatedWhenCommentInvalid() throws Exception {

        var comment = new Comment(1,1,"","","body comment",5);
        String json = """
                {
                      "postId": 1,
                      "id": 1,
                      "version": 5,
                      "name": "",
                      "email": "",
                      "body": "body comment"
                    }
                """;
        when(repository.save(comment)).thenReturn(comment);
        when(repository.findById(1)).thenReturn(Optional.of(comment));
        mockMvc.perform(put("/api/comments/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldDeleteWhenCommentValid() throws Exception {

        doNothing().when(repository).deleteById(1);
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());

        verify(repository,times(1)).deleteById(1);

    }

    @Test
    void shouldNotDeleteWhenCommentInvalid() throws Exception {

        doNothing().when(repository).deleteById(999);
        mockMvc.perform(delete("/api/comments/999"))
                .andExpect(status().isNoContent());

        verify(repository,times(1)).deleteById(999);

    }



}
