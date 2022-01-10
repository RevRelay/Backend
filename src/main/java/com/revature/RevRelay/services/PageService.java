package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@NoArgsConstructor
public class PageService {
    PageRepository pageRepository;
    UserRepository userRepository;
	PostService postService;
    /**
     * @param pageRepository to be autowired
     */
    @Autowired
    public PageService(PageRepository pageRepository, UserRepository userRepository,PostService postService) {
        this.pageRepository = pageRepository;
        this.userRepository = userRepository;
		this.postService = postService;
    }

    // CREATE

    /**
     * @param page page to be created
     * @return Page the page that is being created
     */
    public Page createPage(Page page) {
        return pageRepository.save(page);
    }

    // READ

    /**
     * @return admin method to get all pages that exist
     */
    public List<Page> getAll() {
        return pageRepository.findAll();
    }

    /**
     * @param userOwnerID the userOwnerID to find the page of
     * @return Page the associated Page with the provided userOwnerID
     */
    public Page getPageByUserOwnerUserID(Integer userOwnerID) {
        return pageRepository.getPageByUserOwnerUserID(userOwnerID);
    }

    /**
     * @param groupID the groupID to find the page of
     * @return Page the associated Page with the provided groupID
     */
    public Page getPageByGroupID(Integer groupID) {
        return pageRepository.getPageByGroupOwnerGroupID(groupID);
    }

    /**
     * @param pageID the pageID to find the page of
     * @return Page the associated Page with the provided pageID
     */
    public Page getPageByPageID(Integer pageID) throws Exception {
        return pageRepository.findById(pageID).orElseThrow(() -> new Exception("No Page Found"));
    }

    // UPDATE

    /**
     * @param page the page to be updated
     * @return Page the page that was updated with new values
     */
    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    // DELETE

    /**
     * @param pageID the pageID to delete from the database
     */
    public void deletePageByID(Integer pageID) {
        pageRepository.deleteById(pageID);
    }

    /**
     * This method gets all the friends from a user
     * @param username of the user you wish to update
     * @return List of friends
     * @throws Exception
     */
    public Set<User> getAllFriendsFromUser(String username) throws Exception {
          // return userRepository.
        System.out.println(username);
        System.out.println("do you see me");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("No friends Found"));
        Set<User> friends = user.getFriends();
        return friends;
    }

	/**
	 * Clears Constraints and Deletes Page
	 * @param page page to Delete
	 * @return boolean of if page is deleted
	 */
	public boolean delete(Page pageToDelete) {
		if (pageToDelete==null)
			return false;
		Page page = pageRepository.findById(pageToDelete.getPageID()).orElse(null);
		if (page==null)
			return false;
		Set<Post> posts = page.getPosts();
		page.setPosts(null);
		page=pageRepository.save(page);
		posts.forEach(post -> postService.delete(post));

		if (page.getGroupOwner() != null) {
			Group g = page.getGroupOwner();
			g.setGroupPage(null);
			page.setGroupOwner(null);
			System.out.println("REMOVING PAGE FROM PARENT");
		}
		if (page.getUserOwner() != null) {
			User user = page.getUserOwner();
			user.setUserPage(null);
			page.setUserOwner(null);
			System.out.println("REMOVING PAGE FROM PARENT");
			userRepository.save(user);
		}

		page=pageRepository.save(page);
		pageRepository.delete(page);
		return true;

	}
	/**
	 * Deletes all pages
	 * @return count of deleted groups
	 */
	public int deleteAll(){
		AtomicInteger count = new AtomicInteger(0);
		List<Page> pages = pageRepository.findAll();
		pages.forEach(page -> {if(delete(page)) count.getAndIncrement();});
		System.out.println("Deleted "+count+" Pages");
		return count.get();
	}
}
