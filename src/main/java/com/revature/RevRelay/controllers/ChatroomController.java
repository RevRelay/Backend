package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.services.ChatroomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller endpoints for Chatroom model

 * @author Ryan Haynes (Intern Devops Engineer)
 * @version 1.17
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/chat")
public class ChatroomController {
	private ChatroomService chatroomService;


	/**
	 * Creates Chatroom Controller
	 * @param chatroomService chatroom service
	 */
	@Autowired
	public ChatroomController(ChatroomService chatroomService){
		this.chatroomService=chatroomService;
	}

	/**
	 * Gets all chatrooms
	 * @return Pageable of all Chatrooms
	 */
	@GetMapping
	public ResponseEntity<?> getAllChatroom(){
		return ResponseEntity.ok(chatroomService.findAll());
	}

	/**
	 * Updates existing Chatroom
	 * @param chatID chatroom id to update
	 * @param chatroom new data
	 * @return updated chatroom
	 */
	@PutMapping("/{chatID}")
	public ResponseEntity<?> updateChatroom(@PathVariable Integer chatID,@RequestBody Chatroom chatroom){
		chatroom.setChatID(chatID);
		return ResponseEntity.ok(chatroomService.save(chatroom));

	}

	/**
	 * Gets chatroom by id
	 * @param chatID chatroom id
	 * @return chatroom
	 */
	@GetMapping("/{chatID}")
	public ResponseEntity<?> getByID(@PathVariable Integer chatID){
		Chatroom chatroom = chatroomService.findByID(chatID);
		if (chatroom==null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(chatroom);
	}

	/**
	 * Creates new chatroom
	 * @param chatroom chatroom data
	 * @return new chatroom
	 */
	@PostMapping
	public ResponseEntity<?> createNew(@RequestBody Chatroom chatroom){
		return ResponseEntity.ok(chatroomService.save(chatroom));
	}

	/**
	 * Adds new member to chatroom
	 * @param chatID chatroom id
	 * @param userID users id
	 * @return chatroom with new member
	 */
	@PostMapping("/{chatID}/addUser")
	public ResponseEntity<?> addNewMember(@PathVariable Integer chatID,@RequestParam("userID") int userID){
		return ResponseEntity.ok(chatroomService.addMember(chatID,userID));

	}
	/**
	 * Deletes member from chatroom
	 * @param chatID chatroom id
	 * @param userID user id
	 * @return chatroom without members
	 */
	@PostMapping("/{chatID}/removeUser")
	public ResponseEntity<?> removeMember(@PathVariable Integer chatID,@RequestParam("userID") int userID){
		return ResponseEntity.ok(chatroomService.removeMember(chatID,userID));
	}
}
