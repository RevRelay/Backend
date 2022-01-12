package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.services.ChatroomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller endpoints for Chatroom model
 *
 * @author Ryan Haynes (Intern Devops Engineer)
 * @version 1.17
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/chat")
public class ChatroomController {
	private ChatroomService chatroomService;

	/**
	 * Creates the Chatroom Controller.
	 *
	 * @param chatroomService The service layer for chatrooms.
	 */
	@Autowired
	public ChatroomController(ChatroomService chatroomService){
		this.chatroomService=chatroomService;
	}

	/**
	 * Gets all chatrooms.
	 *
	 * @return Pageable of all Chatrooms.
	 */
	@GetMapping
	public ResponseEntity<?> getAllChatroom(){
		return ResponseEntity.ok(chatroomService.findAll());
	}

	/**
	 * Updates the existing Chatroom with the given ID.
	 *
	 * @param chatID The chatID to for the chatroom to be updated.
	 * @param chatroom New chatroom data.
	 * @return Updated chatroom.
	 */
	@PutMapping("/{chatID}")
	public ResponseEntity<?> updateChatroom(@PathVariable Integer chatID,@RequestBody Chatroom chatroom){
		chatroom.setChatID(chatID);
		return ResponseEntity.ok(chatroomService.save(chatroom));
	}

	/**
	 * Gets the chatroom by its ID.
	 *
	 * @param chatID The chatroom ID.
	 * @return Returns the chatroom with the given chatID. If none exists then it returns a 404 response.
	 */
	@GetMapping("/{chatID}")
	public ResponseEntity<?> getByID(@PathVariable Integer chatID){
		Chatroom chatroom = chatroomService.findByID(chatID);
		if (chatroom==null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(chatroom);
	}

	/**
	 * Gets all chatrooms for a given user by their userID.
	 *
	 * @param userID The current user's userID.
	 * @return Returns pageable of all chats a user with the given userID. If none exists then it returns a 404 response.
	 */
	@GetMapping("/member/{userID}")
	public ResponseEntity<?> getAllByUserID(@PathVariable Integer userID){
		Page<Chatroom> chatrooms = chatroomService.findAllChatsByUserID(userID);
		if (chatrooms==null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(chatrooms);
	}

	/**
	 * Creates new chatroom.
	 *
	 * @param chatroom The chatroom's information.
	 * @return The newly created chatroom.
	 */
	@PostMapping
	public ResponseEntity<?> createNew(@RequestBody Chatroom chatroom){
		return ResponseEntity.ok(chatroomService.save(chatroom));
	}

	/**
	 * Adds a new member to chatroom.
	 *
	 * @param chatID The chatroom's chatID in which you are adding members.
	 * @param userID The user's userID you are adding to the chatroom.
	 * @return The chatroom with a new member.
	 */
	@PostMapping("/{chatID}/addUser")
	public ResponseEntity<?> addNewMember(@PathVariable Integer chatID,@RequestParam("userID") int userID){
		return ResponseEntity.ok(chatroomService.addMember(chatID,userID));
	}
	
	/**
	 * Deletes a member from chatroom by their userID.
	 *
	 * @param chatID The chatroom's chatID in which you are removing members.
	 * @param userID The user's userID you are removing from the chatroom.
	 * @return The chatroom without the given member.
	 */
	@PostMapping("/{chatID}/removeUser")
	public ResponseEntity<?> removeMember(@PathVariable Integer chatID,@RequestParam("userID") int userID){
		return ResponseEntity.ok(chatroomService.removeMember(chatID,userID));
	}
}
