package com.revature.RevRelay.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Groups {

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
    @JsonBackReference
    private List<Users> members;
}
