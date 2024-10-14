package com.jehad.lion.comment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CommentRepository extends ListCrudRepository<Comment,Integer> {
}
