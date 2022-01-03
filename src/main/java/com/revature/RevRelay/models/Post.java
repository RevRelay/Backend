package com.revature.RevRelay.models;

import com.revature.RevRelay.enums.PostType;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Post model for posts information and relationships. Posts are a part of a page which are owned by a user.
 * Posts can also have a parent post and child posts, with child posts being the replyies of parent posts.
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
    private String postContent;

    @Column(nullable = false)
    private Date postTime;

    @Column(nullable = false)
    private int postOwnerID;

    // post relationships to other models
    @ManyToOne
    private Page postPageID;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Post parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.MERGE)
    private List<Post> children;
}
