package com.revature.RevRelay.models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users implements UserDetails {

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

    @ManyToMany(cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Groups> userGroups;

    @ElementCollection
    @CollectionTable(name = "groupList")
    private List<Integer> ownedGroupIDs;

    @ElementCollection
    @CollectionTable(name = "pageList")
    private List<Integer> userPageID;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Chatroom> chatRooms;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

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
