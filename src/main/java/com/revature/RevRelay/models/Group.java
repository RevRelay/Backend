package com.revature.RevRelay.models;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_generator")
    @SequenceGenerator(name = "group_generator", sequenceName = "group_seq")
    private int groupID;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(nullable = false)
    private int userOwnerID;

    @Column(nullable = false)
    boolean isPrivate;

    @ManyToMany(mappedBy = "userGroups", cascade = CascadeType.MERGE)
    private List<User> members;
}
