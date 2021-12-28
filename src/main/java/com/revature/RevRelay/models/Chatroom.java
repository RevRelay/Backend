package com.revature.RevRelay.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(mappedBy = "chatRooms", cascade = CascadeType.MERGE)
    @JsonBackReference
    private List<User> members;

    @Column(nullable = false)
    private boolean isPrivate;

}
