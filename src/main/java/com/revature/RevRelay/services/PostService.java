package com.revature.RevRelay.services;

import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.repositories.PostRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Constructor for PostService
     *
     * @param postRepository is the data layer for the Group model
     */
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create Methods

    /**
     * Persists a new Post to the database
     * <p>
     * Receives a Post object from the controller and persists it as a new Post
     * record on the database
     *
     * @param post is the Post object to be persisted
     * @return Post is the Post created on the database
     */
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Read Methods

    /**
     * Retrieves all Posts from the database by their ID
     * <p>
     * Receives a Post ID from the controller and queries the database for
     * that Post
     *
     * @param postID
     * @return Post
     */
    public Post findPostByPostID(Integer postID) throws Exception {
        return postRepository.findById(postID).orElseThrow(() -> new Exception("No Posts Found"));
    }

    /**
     * Retrieves all Posts from the database by their PageID
     * <p>
     * Receives a Page ID from the controller and queries the database for
     * every Post which has that Page ID
     *
     * @param pageID is the ID of the page used when querying the database
     * @return Page<Post> is a page containing the Groups
     */
    public Page<Post> findPostByPostPageID(Integer pageID) {
        return postRepository.findAllByPostTypeAndPostPagePageID(PostType.ORIGINAL, pageID, Pageable.unpaged());
    }

    /**
     * Retrieves all Posts from the database by their PageID
     * <p>
     * Receives a Page ID from the controller and queries the database for
     * every Post which has that Page ID Also receives a pageable, that specifies
     * a specific page to return from Page<Post>
     *
     * @param postPageID is the ID of the page used when querying the database
     * @param pageable   is the specific page to return from the Page<Post>
     * @return Page<Post> is page containing the Groups from a specific page
     */
    public Page<Post> findPostByPostPageID(Integer postPageID, Pageable pageable) {
        return postRepository.findAllByPostTypeAndPostPagePageID(PostType.ORIGINAL, postPageID, pageable);
    }

    /**
     * Retrieves all Posts from the database by their OwnerID
     * <p>
     * Receives an Owner ID from the controller and queries the database for
     * every Post which has that OwnerID
     *
     * @param postOwnerID is the ID of the owner used when querying the database
     * @return Page<Post> is page containing the Groups
     */
    public Page<Post> findPostByPostOwnerID(Integer postOwnerID) {
        return postRepository.findByPostOwnerID(postOwnerID, Pageable.unpaged());
    }

    /**
     * Retrieves all Posts from the database by their OwnerID
     * <p>
     * Receives an Owner ID from the controller and queries the database for
     * every Post which has that OwnerID. Also receives a pageable, that specifies
     * a specific page to return from Page<Post>
     *
     * @param postOwnerID is the ID of the owner used when querying the database
     * @param pageable    is the specific page to return from the Page<Group>
     * @return Page<Post> is page containing the Groups from a specific page
     */
    public Page<Post> findPostByPostOwnerID(Integer postOwnerID, Pageable pageable) {
        return postRepository.findByPostOwnerID(postOwnerID, pageable);
    }

    // Update Methods

    /**
     * Updates a Post
     * <p>
     * Receives a Post object from the controller and updates that Post on the
     * database
     *
     * @param post is the Post object to be persisted
     * @return Post is the Post created on the database
     */
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    // Delete Methods

    /**
     * Deletes a Post
     * <p>
     * Receives a Post ID from the controller and deletes the Post with that ID
     * from the database
     *
     * @param postID is the ID of the Post to remove
     */
    public void deletePostByPostID(Integer postID) {
        postRepository.deleteById(postID);
    }

	/**
	 * deletes all posts and sub posts
	 * @param post to delete
	 * @return boolean of deleted page
	 */
	public boolean delete(Post post) {
		Post postToDelete = postRepository.findById(post.getPostID()).orElse(null);
		if (postToDelete==null) return false;
		postToDelete.setPostPage(null);
		postToDelete.setParent(null);
		List<Post> postList = postToDelete.getChildren();
		postToDelete.setChildren(null);

		postToDelete = postRepository.save(postToDelete);
		postList.forEach(this::delete);

		postRepository.delete(postToDelete);
		return true;
	}
	/**
	 * Deletes all posts
	 * @return count of deleted posts
	 */
	public int deleteAll(){
		AtomicInteger count = new AtomicInteger(0);
		List<com.revature.RevRelay.models.Post> posts = postRepository.findAll();
		posts.forEach(post -> {if(delete(post)) count.getAndIncrement();});
		System.out.println("Deleted "+count+" Posts");
		return count.get();
	}
	
	/**
	 * Upvote/Downvotes a post or toggles no vote
	 * @param postID post to vote on
	 * @param userID user who is posting
	 * @param up whether to up vote or downvote
	 * @return Post that was voted on
	 */
	public Post upVotePost(Integer postID,Integer userID, Boolean up) throws Exception {
		Post post = postRepository.findById(postID).orElseThrow(()->new Exception("Post Not Found"));
		Set<Integer> uVoters = post.getUpVoters();
		Set<Integer> dVoters = post.getDownVoters();
		if (up){
			dVoters.remove(userID);
			if (!uVoters.remove(userID)) uVoters.add(userID);
		} else {
			uVoters.remove(userID);
			if (!dVoters.remove(userID)) dVoters.add(userID);
		}
		return postRepository.save(post);
	}


}
