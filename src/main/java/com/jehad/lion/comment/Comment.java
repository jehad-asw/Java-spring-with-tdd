package com.jehad.lion.comment;

import com.jehad.lion.post.Post;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

public record Comment(@Id Integer id, Integer postId, @NotEmpty String name, @NotEmpty String email, String body, @Version Integer version) {
}
