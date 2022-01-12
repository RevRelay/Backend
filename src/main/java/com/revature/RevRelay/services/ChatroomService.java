package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.ChatroomRepository;
import com.revature.RevRelay.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer for Chatroom model
 */
@NoArgsConstructor
@Service
public class ChatroomService {
	private ChatroomRepository chatroomRepository;
	private UserRepository userRepository;

	@Autowired
	public ChatroomService(ChatroomRepository chatroomRepository,UserRepository userRepository){
		this.chatroomRepository = chatroomRepository;
		this.userRepository = userRepository;
	}
	/**
	 * Finds All Chatrooms
	 * @return All chatroom
	 */
	public Page<Chatroom> findAll(){
		return chatroomRepository.findAll(Pageable.unpaged());
	}

	/**
	 * Finds Chatroom by ID
	 * @param chatID chatroom ID
	 * @return chatroom
	 */
	public Chatroom findByID(int chatID){
		return chatroomRepository.findById(chatID).orElse(null);
	}
	/**
	 * saves chatroom
	 * @param chatroom chatroom to save
	 * @return chatroom as it is saved
	 */
	public Chatroom save(Chatroom chatroom	){
		return chatroomRepository.save(chatroom);
	}

	/**
	 * Adds the user to the chatroom
	 * @param chatID chatroomID
	 * @param userID userID
	 * @return chatroom the user was added to
	 */
	public Chatroom addMember(int chatID,int userID){
		Chatroom chatroom = chatroomRepository.findById(chatID).orElse(null);
		User user = userRepository.findById(userID).orElse(null);
		assert (chatroom!=null && user!= null);
		Set<Chatroom> userChatRooms = user.getChatRooms();
		userChatRooms.add(chatroom);
		user.setChatRooms(userChatRooms);
		userRepository.save(user);
		return chatroomRepository.findById(chatID).orElse(null);

	}

	/**
	 * Remove the user from the chatroom
	 * @param chatID chatroomID
	 * @param userID userID
	 * @return chatroom the user was removed form
	 */
	public Chatroom removeMember(int chatID,int userID){
		Chatroom chatroom = chatroomRepository.findById(chatID).orElse(null);
		User user = userRepository.findById(userID).orElse(null);
		assert (chatroom!=null && user!= null);
		user.setChatRooms(user.getChatRooms().stream()
				.filter((userChat) ->
						userChat.getChatID() != chatroom.getChatID()).collect(Collectors.toSet()));
		userRepository.save(user);
		return chatroomRepository.findById(chatID).orElse(null);
	}

	/**
	 * Gets all chatrooms that a user is a member of
	 * @param userID user to search by
	 * @return
	 */
	public Set<Chatroom> getChatroomsByMembers(int userID){
		User user = userRepository.findByUserID(userID).orElse(null);
		assert user!=null;
		return user.getChatRooms();
	}

	public Page<Chatroom> findAllChatsByUserID(Integer userID) {
		return chatroomRepository.findAllByMembersUserID(userID,Pageable.unpaged());
	}
}
