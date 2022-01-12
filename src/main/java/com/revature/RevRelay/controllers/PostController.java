package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.services.PostService;
import com.revature.RevRelay.services.UserService;
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
    private UserService userService;

    /**
     * Constructor for the GroupsController.
     *
     * @param postService The service layer for Posts.
     * @param userService The service layer for Users.
     */
    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Creates a post with the given information.
     *
     * @param post A post with its information.
     * @return  If a post has a display name, then it returns the Post with the given inform.
     *          If not, then it returns a 404 status.
     */
    @PostMapping
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token,@RequestBody Post post) {
		String tokenParsed = token.replace("Bearer", "").trim();
		try {
			post.setPostAuthor(userService.loadUserByToken(tokenParsed).getDisplayName());
			return ResponseEntity.ok(postService.createPost(post));
		} catch (Exception e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
    }

    /**
     * Gets a single Post with the given postID.
     * @param postID The postID for the post to be retrieved.
     * @return  If the postID exists then it returns the requested Post with the requested postID.
     *          If not, then returns a 404 status.
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
	 * Allows a user to upvote or downvote a Post by a postID.
	 * @param postID The postID for the post to be retrieved.
	 * @return  If the postID exists then it returns the requested Post with the
     *          new vote total with the given postID.
     *          If not, then returns a 404 status.
	 */
	@PutMapping("/{postID}/vote")
	public ResponseEntity<?> upvotePost(@RequestHeader("Authorization") String token,
                                        @PathVariable Integer postID,
                                        @RequestParam("upvote") Boolean upvote) {
        String tokenParsed = token.replace("Bearer", "").trim();
		try {
			return ResponseEntity.ok(postService.upVotePost(postID, userService.loadUserByToken(tokenParsed).getUserID(), upvote));
		} catch (Exception e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	/**
     * Gets all the posts associated with a page by postPageID.
     *
     * @param postPageID The ID of the page associated with the posts.
     * @return A pageable of all the posts associated with the queried page.
     */
    @GetMapping("/page/{postPageID}")
    public Page<Post> findPostByPostPageID(@PathVariable Integer postPageID) {
        return postService.findPostByPostPageID(postPageID);
    }
    // TODO: overload ^ to accept pageable parameters

    /**
     * Gets all the posts associated with an owner by postOwnerID.
     *
     * @param postOwnerID The ID of the user associated with the posts.
     * @return a pageable of all the posts associated with the queried user.
     */
    @GetMapping("/user/{postOwnerID}")
    public Page<Post> findPostByPostOwnerID(@PathVariable Integer postOwnerID) {
        return postService.findPostByPostOwnerID(postOwnerID);
    }
    // TODO: overload ^ to accept pageable parameters

    /**
     * Update a post with the given post information.
     *
     * @param post the new post we are passing in.
     * @return  Updates the post with the given info then it returns the requested
     *          Post with the new vote total with the given postID.
     */
    @PutMapping
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(post));
    }

    /**
     * Delete a single post by postID.
     * @param postID The postID of the post to be deleted.
     */
    @DeleteMapping("/{postID}")
    public void deletePostByPostID(@PathVariable Integer postID) {
        postService.deletePostByPostID(postID);
    }
}
