package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GroupServiceTest {
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	GroupService groupService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ChatroomRepository chatroomRepository;
	@Autowired
	PostRepository postRepo;
	@Autowired
	PageRepository pageRepo;

	@Autowired
	UserService userService;
	@Autowired
	PostService postService;

	@Autowired
	PageService pageService;



	private final String testUsername = "fakeUser";
	private final String testPassword = "testPassword";
	private final String testEmail = "fakeEmail";
	private final String testDisplayName = "fakeDisplayName";
	private User user;

	@BeforeEach
	public void setup(){
		System.out.println("users Before:"+userRepository.findAll().size());
		userService.deleteAll();
		System.out.println("users After:"+userRepository.findAll().size());

		System.out.println("Groups Before:"+groupRepository.findAll().size());
		groupService.deleteAll();
		System.out.println("Groups After:"+groupRepository.findAll().size());

		postService.deleteAll();
		pageService.deleteAll();

		this.user = new User();
		user.setUsername(testUsername);
		user.setPassword(testPassword);
		user.setEmail(testEmail);
		user.setDisplayName(testDisplayName);
		userRepository.save(user);
	}

	public GroupServiceTest() {
	}

	@Test
	public void createGroupTest() {
		System.out.println(groupRepository);
		Group group = new Group();
		group.setGroupName("TEST");
		group.setUserOwner(user);
		Group group1 = groupService.createGroup(group);
		assertEquals(group.getGroupName(), group1.getGroupName());
		assertEquals(group1.getGroupID(), groupService.getGroupByGroupID(group1.getGroupID()).getGroupID());
	}

	@Test
	public void NoArgsTest() {
		GroupService gr = new GroupService();
		assertNotNull(gr);
	}

	@Test
	public void getAllTest() {

		User user1 = userRepository.save(user);
		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Group g = new Group();
			g.setGroupName(i + "");
			g.setUserOwner(user1);
			g.setPrivate(false);
			g = groupService.createGroup(g);
			groups.add(g);

		}
		Page<Group> p1 = groupService.getAll();
		Page<Group> p2 = groupService.getAll(Pageable.unpaged());
		System.out.println(p2.getTotalElements());
		for (int i = 0; i < 100; i++) {
			assertEquals(groups.get(i).getGroupID(), p1.getContent().get(i).getGroupID());
			assertEquals(groups.get(i).getGroupID(), p2.getContent().get(i).getGroupID());
		}
	}

	@Test
	public void findAllByOwnerIDTest() {
		groupRepository.deleteAll();
		chatroomRepository.deleteAll();
		userRepository.deleteAll();

		User user1 = userRepository.save(user);

		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Group g = new Group();
			g.setGroupName(i + "");
			g.setUserOwner(user1);
			g.setPrivate(false);
			groups.add(g);
			groupService.createGroup(g);
		}
		List<Group> groups1 = groupRepository.findAll();
		for (int i = 0; i < 100; i++) {
			assertEquals(groups1.get(i).getGroupID(), groups.get(i).getGroupID());
		}
	}
	
	@Test
	public void findAllByOwnerIDTestPageable() {
		groupRepository.deleteAll();
		chatroomRepository.deleteAll();
		userRepository.deleteAll();

		User user1 = userRepository.save(user);

		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Group g = new Group();
			g.setGroupName(i + "");
			g.setUserOwner(user1);
			g.setPrivate(false);
			groups.add(g);
			groupService.createGroup(g);
		}
		List<Group> groups1 = groupRepository.findAll();
		for (int i = 0; i < 100; i++) {
			assertEquals(groups1.get(i).getGroupID(), groups.get(i).getGroupID());
		}
	}

	@Test
	public void updateGroupsTestTest() {
		groupRepository.deleteAll();
		System.out.println(groupRepository);
		Group group = new Group();
		group.setGroupName("TEST");
		group.setUserOwner(user);
		Group group1 = groupService.createGroup(group);
		group1.setGroupName("Test2");
		assertEquals(group1.getGroupName(), groupService.updateGroups(group1).getGroupName());
	}

	@Test
	public void deleteGroupsByIDTest() {
		Group group = new Group();


		group.setGroupID(10000);
		group.setGroupName("TEST");
		group.setUserOwner(user);

		Group group1 = groupService.createGroup(group);
		groupService.deleteGroupsByID(group1.getGroupID());
		Group g = groupService.getGroupByGroupID(10000);
		assertNull(g);
	}
	@Test
	public void addMemberTest(){
		User user1 = new User();
		user1.setUsername("user112");
		user1.setPassword("pass");
		user1.setEmail("1@2.com");
		user1.setDisplayName("user1");
		user1 = userRepository.save(user1);
		User user2 = new User();
		user2.setUsername("user223");
		user2.setPassword("pass");
		user2.setEmail("2@2.com");
		user2.setDisplayName("user2");
		user2 = userRepository.save(user2);
		Group group = new Group();
//		group.setUserOwner(user1);
		group.setGroupName("name");
		group = groupRepository.save(group);
		groupService.addMember(group.getGroupID(),user2.getUserID());
		Set<User> memberList = groupService.getGroupByGroupID(group.getGroupID()).getMembers();
		User finalUser = user2;
		memberList.forEach(member -> assertEquals(member.getUserID(), finalUser.getUserID()));
	}
	@Test
	public void deleteMemberTest(){
		User user1 = new User();
		user1.setUsername("user154");
		user1.setPassword("pass");
		user1.setEmail("1@2.com");
		user1.setDisplayName("user1");
		user1 = userRepository.save(user1);
		User user2 = new User();
		user2.setUsername("user2645");
		user2.setPassword("pass");
		user2.setEmail("2@2.com");
		user2.setDisplayName("user2");
		user2 = userRepository.save(user2);
		Group group = new Group();
		group.setUserOwner(user1);
		group.setGroupName("name");
		group = groupRepository.save(group);
		groupService.addMember(group.getGroupID(),user2.getUserID());
		Set<User> memberList = groupService.getGroupByGroupID(group.getGroupID()).getMembers();
		assertEquals(user2.getUserID(),memberList.stream().findFirst().get().getUserID());
		groupService.deleteMember(group.getGroupID(),user2.getUserID());
		assertNull(group.getMembers());
	}
	@Test
	public void findAllMembersByUserIDTest(){
		User user1 = new User();
		user1.setUsername("user111");
		user1.setPassword("pass");
		user1.setEmail("1@3.com");
		user1.setDisplayName("user1");
		user1 = userRepository.save(user1);
		User user2 = new User();
		user2.setUsername("user222");
		user2.setPassword("pass");
		user2.setEmail("2@3.com");
		user2.setDisplayName("user2");
		user2 = userRepository.save(user2);
		Group group = new Group();
		group.setUserOwner(user1);
		group.setGroupName("name");
		System.out.println(group.getUserOwnerID());
		group = groupRepository.save(group);
		groupService.addMember(group.getGroupID(),user2.getUserID());
		Set<User> memberList = groupService.getGroupByGroupID(group.getGroupID()).getMembers();
		assertEquals(memberList.stream().findFirst().get().getUserID(),user2.getUserID());

		Page<Group> groups = groupService.findAllMembersByUserID(user1.getUserID());
		assertEquals(group.getGroupID(),groups.getContent().get(0).getGroupID());
	}
	@Test
	public void findAllMembersByUserIDTestPageable(){
		User user1 = new User();
		user1.setUsername("user183");
		user1.setPassword("pass");
		user1.setEmail("1@4.com");
		user1.setDisplayName("user1");
		user1 = userRepository.save(user1);
		User user2 = new User();
		user2.setUsername("user224");
		user2.setPassword("pass");
		user2.setEmail("2@4.com");
		user2.setDisplayName("user2");
		user2 = userRepository.save(user2);
		Group group = new Group();
		group.setUserOwnerID(user1.getUserID());
		group.setGroupName("name");
		group = groupRepository.save(group);
		groupService.addMember(group.getGroupID(),user2.getUserID());
		Set<User> memberList = groupService.getGroupByGroupID(group.getGroupID()).getMembers();
		assertEquals(memberList.stream().findFirst().get().getUserID(),user2.getUserID());

		Page<Group> groups = groupService.findAllMembersByUserID(user1.getUserID(),Pageable.unpaged());
		assertEquals(group.getGroupID(),groups.getContent().get(0).getGroupID());
	}
}
