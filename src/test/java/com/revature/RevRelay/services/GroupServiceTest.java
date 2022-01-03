package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

	User user;

	public GroupServiceTest() {
		this.user = new User();
		user.setUsername("fakeUser");
		user.setPassword("fakePassword");
		user.setEmail("fakeEmail");
		user.setDisplayName("fakeDisplayName");
	}

	@Test
	public void createGroupTest() {
		System.out.println(groupRepository);
		Group group = new Group();
		group.setGroupName("TEST");
		Group group1 = groupService.createGroup(group);
		assertEquals(group, group1);
		assertEquals(group1.getGroupID(), groupService.getGroupByGroupID(group1.getGroupID()).getGroupID());
	}

	@Test
	public void NoArgsTest() {
		GroupService gr = new GroupService();
		assertNotNull(gr);
	}

	@Test
	public void getAllTest() {
		groupRepository.deleteAll();
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
		Page<Group> p1 = groupService.getAll();
		Page<Group> p2 = groupService.getAll(Pageable.unpaged());
		for (int i = 0; i < 100; i++) {
			assertEquals(groups.get(i).getGroupID(), p1.getContent().get(i).getGroupID());
			assertEquals(groups.get(i).getGroupID(), p2.getContent().get(i).getGroupID());
		}
	}

	@Test
	public void findAllByOwnerIDTest() {
		groupRepository.deleteAll();
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
		Group group1 = groupService.createGroup(group);
		group1.setGroupName("Test2");
		assertEquals(group1.getGroupName(), groupService.updateGroups(group1).getGroupName());
	}

	@Test
	public void deleteGroupsByIDTest() {
		groupRepository.deleteAll();
		Group group = new Group();
		group.setGroupID(10000);
		group.setGroupName("TEST");
		Group group1 = groupService.createGroup(group);
		groupService.deleteGroupsByID(group1.getGroupID());
		Group g = groupService.getGroupByGroupID(10000);
		assertNull(g);
	}
}
