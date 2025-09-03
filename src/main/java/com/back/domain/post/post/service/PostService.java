package com.back.domain.post.post.service;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public long count() {
        return 0;
    }

    public Post create(String title, String content) {
        Post post = new Post(title, content);
        return postRepository.save(post);
    }

    public void update(Post post, String title, String content) {
        post.modify(title, content);
    }

    public List<Post> getList() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시글이 존재하지 않습니다.")
        );
    }
}
