package com.revature.RevRelay.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Page Model
 * 
 * Holds information and relationships for Pages. Every User and Group has one
 * Page. Pages contain many Posts. Pages have a User as an Owner, but can also
 * be associated to a Group.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
	private int pageID;
	private String description;
	private String bannerURL;
	private boolean isPrivate;
}
