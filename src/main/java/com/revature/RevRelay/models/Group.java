package com.revature.RevRelay.models;

import lombok.*;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

/**
 * Group Model
 * 
 * Holds information and relationships for Groups. Every Group has a page,
 * memebers, and an owner.
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
    @JsonManagedReference(value = "group-page")
    private Page groupPage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userOwnerID")
    @JsonBackReference
    private User userOwner;

    @Column(nullable = false)
    boolean isPrivate;

    // group relations to other models
    @ManyToMany(mappedBy = "userGroups", cascade = CascadeType.MERGE)
    private List<User> members;
}
