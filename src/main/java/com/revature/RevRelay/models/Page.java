package com.revature.RevRelay.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.List;

/**
 * Page model for page information and relationships. Pages are either owned by
 * a User
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value="user-page")
    private User userOwner;

    @OneToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value="group-page")
    private Group groupOwner;

    @OneToMany(mappedBy = "postPage", cascade = CascadeType.MERGE)
    @JsonManagedReference(value="page-post")
    private List<Post> posts;
}
