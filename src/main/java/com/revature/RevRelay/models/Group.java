package com.revature.RevRelay.models;

import com.revature.RevRelay.repositories.UserRepository;
import com.revature.RevRelay.services.UserService;
import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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

    @Autowired
    @Transient
    UserRepository userRepository;

    // group information
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_generator")
    @SequenceGenerator(name = "group_generator", sequenceName = "group_seq")
    private int groupID;

    @Column(nullable = false, unique = true)
    private String groupName;

    @OneToOne
    @Cascade({CascadeType.MERGE, CascadeType.DELETE})
    @JsonManagedReference(value = "group-page")
    private Page groupPage;

//    @ManyToOne
//    //@Cascade({CascadeType.MERGE, CascadeType.DETACH})
//    @JoinColumn(name = "userOwnerID")
//    @JsonBackReference
//    private User userOwner;
    @Column
    private int userOwnerID;

    @Column(nullable = false)
    boolean isPrivate;

    // group relations to other models
    @ManyToMany(mappedBy = "userGroups")
    @Cascade(CascadeType.DETACH)
    private List<User> members;

    public void setUserOwner(User user) {
        this.userOwnerID = user.getUserID();
    }

//    public User getUserOwner() {
//        return userRepository.findById(userOwnerId).orElse(null);
//    }
}
