package com.revature.RevRelay.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CascadeType;

/**
 * User model containing all user information. username and password are used
 * for login authentication, users own a page
 * and can hold multiple group pages, and users can be a part of multiple chat
 * rooms and multiple groups.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    // User information
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
    private int userID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Date birthDate;

    @Column(nullable = false)
    private String displayName;

    // User Relations to other models
    @ManyToMany
    @Cascade(CascadeType.MERGE)
    @JsonIgnore
    private Set<Group> userGroups;

    @OneToMany
    @Cascade({CascadeType.MERGE})
//    @JsonManagedReference
    private Set<Group> ownedGroups;

    @OneToOne
    @Cascade({CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DELETE})
    @JsonManagedReference(value = "user-page")
    private Page userPage;

    @ManyToMany
    @Cascade(CascadeType.MERGE)
    @JsonIgnore
    private Set<Chatroom> chatRooms;

    @ManyToMany
    @Cascade({CascadeType.DETACH})
    @JsonIgnore
    private Set<User> friends;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * Methods used for Spring Security for a secure user login
     *
     * @return booleans for corresponding security functions
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
