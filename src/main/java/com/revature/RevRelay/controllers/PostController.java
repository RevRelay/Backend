package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.services.PostService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Isaiah Anason
 * @author Evan Ritchey
 * @author Ryan Haynes
 * @author Luis Loustaunau
 * @author Noah Frederick (Even though he showed up late today :( )
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * create a post
     * @param post the fresh post we want created
     * @return status 200, post successfully created
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    /**
     * get a SINGULAR post directly by its ID
     * @param postID of the corresponding post
     * @return the valid post, or status 404 if the ID is invalid
     */
    @GetMapping("/{postID}")
    public ResponseEntity<?> findPostByPostID(@PathVariable Integer postID) {
        try {
            return ResponseEntity.ok(postService.findPostByPostID(postID));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * get all the posts associated with a page ID
     * @param postPageID ID of the page associated with the posts
     * @return a paggable of all the posts associated with the queried page
     */
    @GetMapping("/page/{postPageID}")
    public Page<Post> findPostByPostPageID(@PathVariable Integer postPageID) {
        return postService.findPostByPostPageID(postPageID);
    }
    // TODO: overload ^ to accept pageable parameters

    /**
     * get all the posts associated with an owner ID
     * @param postOwnerID ID of the user associated with the posts
     * @return a paggable of all the posts associated with the queried user
     */
    @GetMapping("/user/{postOwnerID}")
    public Page<Post> findPostByPostOwnerID(@PathVariable Integer postOwnerID) {
        return postService.findPostByPostOwnerID(postOwnerID);
    }
    // TODO: overload ^ to accept pageable parameters

    /**
     * update a post
     * @param post the new post we're passing in via. the HTTP Request Body
     * @return status 200, post successfully updated
     */
    @PutMapping
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(post));
    }

    /**
     * delete a SINGULAR post
     * @param postID ID of the associated post
     */
    @DeleteMapping("/{postID}")
    public void deletePostByPostID(@PathVariable Integer postID) {
        postService.deletePostByPostID(postID);
    }

}
