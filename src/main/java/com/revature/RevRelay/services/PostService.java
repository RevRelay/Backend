package com.revature.RevRelay.services;

import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.repositories.PostRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer for the Post model
 * 
 * @author Isaiah Anason
 * @author Noah Frederick
 * @author Loustauanau Luis
 * @author Evan Ritchey
 * @author Ryan Haynes
 */
@Service
@NoArgsConstructor
public class PostService {
    PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public Post findPostByPostID(Integer postID) throws Exception {
        return postRepository.findById(postID).orElseThrow(() -> new Exception("No Posts Found"));
    }

    public Page<Post> findPostByPostPageID(Integer postPageID) {
        return postRepository.findAllByPostTypeAndPostPagePageID(PostType.ORIGINAL, postPageID, Pageable.unpaged());
    }

    public Page<Post> findPostByPostPageID(Integer postPageID, Pageable pageable) {
        return postRepository.findAllByPostTypeAndPostPagePageID(PostType.ORIGINAL, postPageID, pageable);
    }

    public Page<Post> findPostByPostOwnerID(Integer postOwnerID) {
        return postRepository.findByPostOwnerID(postOwnerID, Pageable.unpaged());
    }

    public Page<Post> findPostByPostOwnerID(Integer postOwnerID, Pageable pageable) {
        return postRepository.findByPostOwnerID(postOwnerID, pageable);
    }

    public void deletePostByPostID(Integer postID) {
        postRepository.deleteById(postID);
    }
}
