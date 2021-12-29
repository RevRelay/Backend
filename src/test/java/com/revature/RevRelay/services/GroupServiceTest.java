package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.repositories.GroupRepository;
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

import java.util.ArrayList;
import java.util.List;

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

	@Test
	public void createGroupTest(){
		System.out.println(groupRepository);
		Group group = new Group();
		group.setGroupName("TEST");
		Group group1 = groupService.createGroup(group);
		assertEquals(group,group1);
		assertEquals(group1.getGroupID(),groupService.getGroupByGroupID(group1.getGroupID()).getGroupID());
	}
	@Test
	public void NoArgsTest(){
		GroupService gr = new GroupService();
		assertNotNull(gr);
	}

	@Test
	public void getAllTest(){
		groupRepository.deleteAll();
		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Group g = new Group();
			g.setGroupName(i+"");
			g.setUserOwnerID(i);
			g.setPrivate(false);
			groups.add(g);
			groupService.createGroup(g);
		}
		Page<Group> p1 =groupService.getAll();
		Page<Group> p2 =groupService.getAll(Pageable.unpaged());
		for (int i = 0; i < 100; i++) {
			assertEquals(groups.get(i).getGroupID(),p1.getContent().get(i).getGroupID());
			assertEquals(groups.get(i).getGroupID(),p2.getContent().get(i).getGroupID());
		}
	}
	@Test
	public void findAllByOwnerIDTest(){
		groupRepository.deleteAll();
		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Group g = new Group();
			g.setGroupName(i+"");
			g.setUserOwnerID(i%10);
			g.setPrivate(false);
			groups.add(g);
			groupService.createGroup(g);
		}
		Page<Group> p1 =groupService.findAllByUserOwnerID(1);
		Page<Group> p2 =groupService.findAllByUserOwnerID(1,Pageable.unpaged());
		for (Group g:p1.getContent()) {
			assertEquals(1,g.getUserOwnerID());
		}
		for (Group g:p2.getContent()) {
			assertEquals(1,g.getUserOwnerID());
		}

	}
	@Test
	public void updateGroupsTestTest(){
		groupRepository.deleteAll();
		System.out.println(groupRepository);
		Group group = new Group();
		group.setGroupName("TEST");
		Group group1 = groupService.createGroup(group);
		group1.setGroupName("Test2");
		assertEquals(group1.getGroupName(),groupService.updateGroups(group1).getGroupName());

	}
	@Test
	public void deleteGroupsByIDTest(){
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
