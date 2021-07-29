package models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "gr")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gr_id", unique = true)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(cascade = CascadeType.REFRESH )
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "gr_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members;

    public Group() {
    }

    public Group(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
