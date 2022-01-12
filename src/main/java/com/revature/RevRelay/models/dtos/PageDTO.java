package com.revature.RevRelay.models.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Set;

/**
 * Page Model
 * 
 * Holds information and relationships for Pages. Every User and Group has one
 * Page. Pages contain many Posts. Pages have a User as an Owner, but can also
 * be be associated to a Group
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
