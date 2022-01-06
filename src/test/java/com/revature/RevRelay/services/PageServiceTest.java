package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.ChatroomRepository;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PageServiceTest {
	@Autowired
	PageRepository pageRepository;
	@Autowired
	PageService pageService;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	ChatroomRepository chatroomRepository;

	User user;
	Group group;

	@BeforeEach
	public void test() {
		pageRepository.deleteAll();
		groupRepository.deleteAll();
		chatroomRepository.deleteAll();
		userRepository.deleteAll();

		this.user = new User();
		user.setUsername("fakeUser");
		user.setPassword("fakePassword");
		user.setEmail("fakeEmail");
		user.setDisplayName("fakeDisplayName");

		this.group = new Group();
		group.setGroupName("fakeGroup");
	}

	@Test
	public void createPageTest() throws Exception {
		System.out.println(pageRepository);
		Page page = new Page();
		page.setDescription("TEST");
		Page Page1 = pageService.createPage(page);
		assertEquals(page, Page1);
		assertEquals(Page1.getPageID(), pageService.getPageByPageID(Page1.getPageID()).getPageID());
	}

	@Test
	public void NoArgsTest() {
		PageService gr = new PageService();
		assertNotNull(gr);
	}

	@Test
	public void getAllTest() {

		List<Page> Pages = new ArrayList<>();
		userRepository.save(user);
		for (int i = 0; i < 100; i++) {
			Page g = new Page();
			g.setDescription(i + "");
			g.setUserOwner(user);
			g.setPrivate(false);
			Pages.add(g);
			pageService.createPage(g);
		}
		List<Page> p1 = pageService.getAll();
		for (int i = 0; i < 100; i++) {
			assertEquals(Pages.get(i).getPageID(), p1.get(i).getPageID());
		}
	}

	@Test
	public void updatePagesTestTest() {
		System.out.println(pageRepository);
		Page Page = new Page();
		Page.setDescription("TEST");
		Page Page1 = pageService.createPage(Page);
		Page1.setDescription("Test2");
		assertEquals(Page1.getDescription(), pageService.updatePage(Page1).getDescription());
	}

	@Test
	public void deletePagesByIDTest() throws Exception {
		pageRepository.deleteAll();
		Page page1 = new Page();
		Page page = pageService.createPage(new Page());
		pageService.deletePageByID(page.getPageID());
		try {
			page1 = pageService.getPageByPageID(page.getPageID());
		} catch (Exception e) {
			page1 = null;
		}
		assertNull(page1);
	}

	@Test
	public void getPageByUserOwnerIDTest() {
		userRepository.save(user);

		Page page = new Page();
		page.setUserOwner(user);
		Page Page1 = pageService.createPage(page);
		assertEquals(Page1.getPageID(), pageService.getPageByUserOwnerUserID(Page1.getUserOwner().getUserID())
				.getPageID());
	}

	@Test
	public void getPageByGroupIDTest() {
		pageRepository.deleteAll();
		groupRepository.save(group);
		Page page = new Page();
		page.setGroupOwner(group);
		Page Page1 = pageService.createPage(page);
		assertEquals(page, Page1);
		assertEquals(Page1.getPageID(), pageService.getPageByGroupID(Page1.getGroupOwner().getGroupID()).getPageID());
	}

	@Test
	public void getAllFriendsFromUserTest() throws Exception {
		boolean friendFound = false;

		User user = new User();
		user.setUsername("fakeUser");
		user.setPassword("fakePassword");
		user.setEmail("fakeEmail@");
		user.setDisplayName("fakeDisplayName");
		userRepository.save(user);
		User friend = new User();
		friend.setUsername("fakeUserrr");
		friend.setPassword("fakePasswordrr");
		friend.setEmail("fakeEmailrr");
		friend.setDisplayName("fakeDisplayNamerrr");
		userRepository.save(friend);

		userService.addFriend(user.getUserID(), friend.getUsername());
		List<User> friends = pageService.getAllFriendsFromUser(user.getUsername());
		for (User friend1 : friends) {
			if (friend1.getUsername().equals(friend.getUsername())) {
				friendFound = true;
			}
		}
		assertEquals(friendFound, true);
	}
}
