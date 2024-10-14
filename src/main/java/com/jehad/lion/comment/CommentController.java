package com.jehad.lion.comment;

import com.jehad.lion.post.ResponseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments/")
public class CommentController {

    private CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("{id}")
    Optional<Comment> findComment(@PathVariable Integer id){
        return commentRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Comment createComment(@RequestBody @Valid Comment comment){
        return commentRepository.save(comment);
    }

    @PutMapping("{id}")
    Comment updateComment(@PathVariable Integer id,
                          @RequestBody @Valid Comment newComment){

        Optional<Comment> existComment = commentRepository.findById(id);
        if(existComment.isPresent()){
            var record = new Comment(existComment.get().id(),
                    existComment.get().postId(),
                    newComment.name(),
                    newComment.email(),
                    newComment.body(),
                    newComment.version());
          return  commentRepository.save(record);
        }else {
            throw new ResponseNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteComment(@PathVariable Integer id){
        commentRepository.deleteById(id);
    }
}
