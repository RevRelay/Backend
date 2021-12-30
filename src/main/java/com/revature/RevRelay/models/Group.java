package com.revature.RevRelay.models;

import lombok.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

/**
 * Group model for group information and relationships. Groups consist of users
 * and group pages are stored in the
 * User model as users are the ones that own group pages
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {

    // group information
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_generator")
    @SequenceGenerator(name = "group_generator", sequenceName = "group_seq")
    private int groupID;

    @Column(nullable = false, unique = true)
    private String groupName;

    @OneToOne(cascade = CascadeType.MERGE)
    @JsonManagedReference(value="group-page")
    private Page groupPage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value="user-group")
    private User userOwner;

    @Column(nullable = false)
    boolean isPrivate;

    // group relations to other models
    @ManyToMany(mappedBy = "userGroups", cascade = CascadeType.MERGE)
    private List<User> members;
}
