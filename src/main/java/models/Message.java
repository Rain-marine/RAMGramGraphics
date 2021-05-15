package models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", unique = true)
    private long id;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(name = "image", columnDefinition = "mediumblob")
    private byte[] image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "grand_sender_id")
    private User grandSender;


    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Message(String text, byte[] image, User sender) {
        this.date = new Date();
        this.text = text;
        this.image = image;
        this.sender = sender;
        this.grandSender = sender;
    }

    public Message(String text, byte[] image, User sender, User receiver) {
        this(text, image, sender);
        this.receiver = receiver;
    }

    public Message() {

    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getGrandSender() {
        return grandSender;
    }

    public void setGrandSender(User grandSender) {
        this.grandSender = grandSender;
    }
}
