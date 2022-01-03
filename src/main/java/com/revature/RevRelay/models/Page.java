package com.revature.RevRelay.models;

import lombok.*;
import javax.persistence.*;

/**
 * Page model for page information and relationships. Pages are either owned by a User
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

    @Column(nullable = false)
    private int userOwnerID;

    @Column
    private int groupID;
}
