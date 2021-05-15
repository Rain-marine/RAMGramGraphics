package models;

import javax.persistence.*;

@Entity
@Table(name = "user_chat")
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_id", unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "has_seen")
    private boolean hasSeen;

    @Column(name = "unseen_count")
    private int unseenCount;


    public UserChat(User user, Chat chat) {
        this.user = user;
        this.chat = chat;
    }

    public UserChat() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    public int getUnseenCount() {
        return unseenCount;
    }

    public void setUnseenCount(int unseenCount) {
        this.unseenCount = unseenCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
