package com.revature.RevRelay.models;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page {

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
