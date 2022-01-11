package com.revature.RevRelay.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.revature.RevRelay.enums.PostType;
import lombok.*;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CascadeType;

/**
 * Post Model
 * 
 * Holds information and relationships for Posts. Every Post is a part of a User
 * or Group Page has one
 * Page.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    // post information
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "post_generator")
    @SequenceGenerator(name = "post_generator", sequenceName = "post_seq")
    private int postID;

    @Column(nullable = false)
    private PostType postType;

    @Column(nullable = false)
    private String postTitle;
	@Column(nullable = false)
	private String postContent;

	@ElementCollection
	Set<Integer> upVoters = new HashSet<>();
	@ElementCollection
	Set<Integer> downVoters= new HashSet<>();

    @Column(nullable = false)
    private Date postTime;

	@Column
	private String postAuthor;

    @Column(nullable = false)
    private int postOwnerID;

    // post relationships to other models
    @ManyToOne // TODO I may be causing issues ;)
    @Cascade(CascadeType.MERGE)
    @JsonBackReference(value = "page-post")
    private Page postPage;

    @ManyToOne
    @Cascade(CascadeType.MERGE)
    @JsonBackReference(value = "post-post")
    private Post parent;

    @OneToMany(mappedBy = "parent")
    @Cascade(CascadeType.MERGE)
    @JsonManagedReference(value = "post-post")
    private List<Post> children;

    @Override
    public String toString() {
        return "Post{" +
                "postID=" + postID +
                ", postType=" + postType +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postTime=" + postTime +
                ", postOwnerID=" + postOwnerID +
                '}';
    }


}
