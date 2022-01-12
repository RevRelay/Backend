package com.revature.RevRelay.controllers;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller endpoints for Chatroom model

 * @author Ryan Haynes (Intern Devops Engineer)
 * @version 1.17
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/validate")
public class ValidateController {

	/**
	 * If you can hit this endpoint your jwt is still valid.
	 *
	 * @return Returns a response entity with an okay.
	 */
	@GetMapping
	public ResponseEntity<?> okayJWTTest(){
		return ResponseEntity.ok("JWT OKAY");
	}
}
