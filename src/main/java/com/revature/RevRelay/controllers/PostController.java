package com.revature.RevRelay.controllers;

/**
 * @author Isaiah Anason
 */

import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.services.PostService;
import javafx.geometry.Pos;
import lombok.NoArgsConstructor;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@NoArgsConstructor
@RestController
@RequestMapping(value="/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        return ResponseEntity.ok(postService.createPost(post));
    }

    @GetMapping("/{postID}")
    public ResponseEntity<Post> findPostByPostID(@PathVariable Integer postID) throws NotFound {
        return ResponseEntity.ok(postService.findPostByPostID(postID));
    }

    @GetMapping("/page/{postPageID}")
    public Page<Post> findPostByPostPageID(@PathVariable Integer postPageID){
        return postService.findPostByPostPageID(postPageID);
    }
    //TODO: overload ^ to accept pageable parameters

    @GetMapping("/user/{postOwnerID}")
    public Page<Post> findPostByPostOwnerID(@PathVariable Integer postOwnerID){
        return postService.findPostByPostOwnerID(postOwnerID);
    }
    //TODO: overload ^ to accept pageable parameters

    @PutMapping
    public ResponseEntity<Post> updatePost(@RequestBody Post post){
        return ResponseEntity.ok(postService.updatePost(post));
    }

    @DeleteMapping("/{postID}")
    public void deletePostByPostID(@PathVariable Integer postID){
        postService.deletePostByPostID(postID);
    }

}
