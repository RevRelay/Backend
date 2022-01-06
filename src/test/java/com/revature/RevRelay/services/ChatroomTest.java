package com.revature.RevRelay.services;


import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ChatroomTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ChatroomRepository chatroomRepository;

	ChatroomService service;

	@BeforeEach
	public void before(){
		service = new ChatroomService(chatroomRepository,userRepository);
		chatroomRepository.deleteAll();
		userRepository.deleteAll();

	}
	@Test
	public void findAllTest(){
		List<Chatroom> chatroomSet = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Chatroom chatroom = new Chatroom();
			chatroom.setRoomName(i+"");
			chatroom.setPrivate(false);
			chatroomSet.add(service.save(chatroom));

		}
		assertEquals(chatroomSet.size(),service.findAll().getContent().size());
	}
	@Test
	public void findByIDTest(){
		List<Chatroom> chatroomSet = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Chatroom chatroom = new Chatroom();
			chatroom.setRoomName(i+"");
			chatroom.setPrivate(false);
			chatroomSet.add(service.save(chatroom));

		}
		assertEquals(chatroomSet.get(1).getRoomName(),service.findByID(chatroomSet.get(1).getChatID()).getRoomName());

	}
	@Test
	public void saveTest(){
		Chatroom chatroom = new Chatroom();
		chatroom.setRoomName("TEST");
		chatroom.setPrivate(false);
		chatroom =service.save(chatroom);
		assertEquals(chatroom.getRoomName(),service.findByID(chatroom.getChatID()).getRoomName());
	}
	@Test
	public void addMemberTest(){
		User user =userRepository.save(new User(1,"test","test","test","test","test",null,"test",null,null,null,new HashSet<>()));
		Chatroom chatroom = new Chatroom();
		chatroom.setChatID(10000000);
		chatroom.setRoomName("TEST");
		chatroom.setPrivate(false);
		chatroom =service.save(chatroom);

		chatroom = service.addMember(chatroom.getChatID(),user.getUserID());
		assertEquals(1,chatroom.getMembers().size());

	}
	@Test
	public void removeMemberTest(){
		User user =userRepository.save(new User(1,"test","test","test","test","test",null,"test",null,null,null,new HashSet<>()));
		Chatroom chatroom = new Chatroom();
		chatroom.setChatID(10000000);
		chatroom.setRoomName("TEST");
		chatroom.setPrivate(false);
		chatroom =service.save(chatroom);

		chatroom = service.addMember(chatroom.getChatID(),user.getUserID());
		assertEquals(1,chatroom.getMembers().size());
		for (User u:chatroom.getMembers()) {
			System.out.println(u.getUserID()+" / "+user.getUserID()
			);
		}
		chatroom = service.removeMember(chatroom.getChatID(),user.getUserID());
		assertEquals(0,chatroom.getMembers().size());
	}
	@Test
	public void getChatroomsByMembersTest(){
		User user =userRepository.save(new User(1,"test","test","test","test","test",null,"test",null,null,null,new HashSet<>()));
		Chatroom chatroom = new Chatroom();
		chatroom.setChatID(10000000);
		chatroom.setRoomName("TEST");
		chatroom.setPrivate(false);
		chatroom =service.save(chatroom);

		chatroom = service.addMember(chatroom.getChatID(),user.getUserID());
		assertEquals(1,chatroom.getMembers().size());

		assertEquals(chatroom.getRoomName(),((Chatroom)service.getChatroomsByMembers(user.getUserID()).toArray()[0]).getRoomName());
	}
}
