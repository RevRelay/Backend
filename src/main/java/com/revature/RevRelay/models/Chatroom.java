package com.revature.RevRelay.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "chatroom_generator")
    @SequenceGenerator(name = "chatroom_generator", sequenceName = "chatroom_seq")
    private int chatID;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<User> members;

    @Column(nullable = false)
    private boolean isPrivate;

	@Column(nullable = false)
	private String roomName;

}
