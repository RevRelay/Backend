package com.revature.RevRelay.models;

import com.revature.RevRelay.enums.PostType;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

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

    @ManyToOne
    private Page postPageID;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Post parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.MERGE)
    private List<Post> children;
}
