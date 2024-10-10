package com.jehad.lion.post;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("")
    List<Post> findAll(){
        return postRepository.findAll();
    }

    @GetMapping("{id}")
    Optional<Post> findById(@PathVariable Integer id){
        return Optional.ofNullable(postRepository.findById(id).orElseThrow(ResponseNotFoundException::new));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Post createPost(@RequestBody @Valid Post post){
        return postRepository.save(post);
    }

    @PutMapping("{id}")
    Post updatePost(@PathVariable Integer id, @RequestBody @Valid Post post){
        Optional<Post> existPost = postRepository.findById(id);
        if(existPost.isPresent()){
            Post updated = new Post(existPost.get().id(),
                    existPost.get().userId(),
                    post.title(),
                    post.body(),
                    post.version());
            return postRepository.save(updated);
        }else{
            throw new ResponseNotFoundException();
        }

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deletePost(@PathVariable Integer id){

        postRepository.deleteById(id);

    }

}
