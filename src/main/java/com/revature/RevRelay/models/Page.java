package com.revature.RevRelay.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.List;

import org.hibernate.annotations.CascadeType;

/**
 * Page Model
 * 
 * Holds information and relationships for Pages. Every User and Group has one
 * Page. Pages contain many Posts. Pages have a User as an Owner, but can also
 * be be associated to a Group
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page {

    // page information
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "page_generator")
    @SequenceGenerator(name = "page_generator", sequenceName = "page_seq")
    private int pageID;

    @Column
    private String description;

    @Column
    private String bannerURL;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column(nullable = false)
    private boolean isGroupPage;

    @OneToOne
    @Cascade(CascadeType.MERGE)
    @JsonBackReference(value = "user-page")
    private User userOwner;

    @OneToOne
    @Cascade({CascadeType.MERGE, CascadeType.DELETE})
    @JsonBackReference(value = "group-page")
    private Group groupOwner;

    @OneToMany(mappedBy = "postPage")
    @Cascade({CascadeType.MERGE, CascadeType.DELETE})
    @JsonManagedReference(value = "page-post")
    private List<Post> posts;


	public Page(User user){
		this.setPosts(null);
		this.setBannerURL("https://i.imgur.com/0EtPsQK.jpeg");
		this.setDescription("You description here");
		this.setUserOwner(user);
		this.setPrivate(true);
		this.setGroupPage(false);
	}

    @Override
    public String toString() {
        return "Page{" +
                "pageID=" + pageID +
                ", description='" + description + '\'' +
                ", bannerURL='" + bannerURL + '\'' +
                ", isPrivate=" + isPrivate +
                ", isGroupPage=" + isGroupPage +
                '}';
    }
}
