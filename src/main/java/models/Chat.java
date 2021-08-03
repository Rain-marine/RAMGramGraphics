package models;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", unique = true)
    private long id;

    @Column(name = "name")
    private String name;

    @Column (name = "is_group")
    private boolean isGroup;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
    private List<UserChat> userChats;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public Chat() {
    }

    public Chat(List<User> users){
        userChats = users.stream().map(it -> new UserChat(it,this)).collect(Collectors.toList());
        this.isGroup = false;
    };

    public Chat(List<User> users , String name){
        this(users);
        this.name = name;
        this.isGroup = true;
    }

    public long getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<UserChat> getUserChats() {
        return userChats;
    }

    public void setUserChats(List<UserChat> userChats) {
        this.userChats = userChats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
