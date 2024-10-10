package com.jehad.lion.post;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PostRepository extends ListCrudRepository<Post,Integer> {
}
