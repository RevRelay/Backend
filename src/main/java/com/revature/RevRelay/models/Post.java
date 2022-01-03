package com.revature.RevRelay.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.revature.RevRelay.enums.PostType;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Post Model
 * 
 * Holds information and relationships for Posts. Every Post is a part of a User
 * or Group Page. Posts have childern/parent posts for replies
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

	@Column(nullable = false)
	private int postLikes;

    @Column(nullable = false)
    private Date postTime;

    @Column(nullable = false)
    private int postOwnerID;

    // post relationships to other models
    @ManyToOne(cascade = CascadeType.MERGE) // TODO I may be causing issues ;)
    @JsonBackReference(value = "page-post")
    private Page postPage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value = "post-post")
    private Post parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.MERGE)
    @JsonManagedReference(value = "post-post")
    private List<Post> children;

    @Override
    public String toString() {
        return "Post{" +
                "postID=" + postID +
                ", postType=" + postType +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postLikes=" + postLikes +
                ", postTime=" + postTime +
                ", postOwnerID=" + postOwnerID +
                '}';
    }
}
