package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatController implements Repository {
    private final static Logger log = LogManager.getLogger(ChatController.class);

    public ChatController() {
    }

    public List<Long> getChatsIds() {
        List<Chat> chats = CHAT_REPOSITORY.getAllChats(LoggedUser.getLoggedUser().getId());
        List<Long> chatIds = new ArrayList<>();
        for (Chat chat : chats) {
            chatIds.add(chat.getId());
        }
        return chatIds;
    }

    public void seeChat(long chatId) {
        log.info("the chat " + chatId + " was seen by :" + LoggedUser.getLoggedUser().getUsername());
        CHAT_REPOSITORY.clearUnSeenCount(chatId, LoggedUser.getLoggedUser().getId());
    }

    public void addMessageToChat(long chatId, String message, byte[] images) {
        long frontUserId = getFrontUserId(chatId);
        Message newMessage = new Message(message, images,
                USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()),
                USER_REPOSITORY.getById(frontUserId));
        CHAT_REPOSITORY.addMessageToChat(chatId, newMessage);
        log.info(LoggedUser.getLoggedUser().getUsername() + " sent message to " + chatId);
    }

    public long getFrontUserId(long chatId) {
        User frontUser = getFrontUser(chatId);
        return frontUser.getId();
    }

    private User getFrontUser(long chatId) {
        Chat chat = CHAT_REPOSITORY.getById(chatId);
        User frontUser = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                ? chat.getUserChats().get(1).getUser()
                : chat.getUserChats().get(0).getUser();
        return frontUser;
    }

    public ArrayList<Long> getMessages(long chatID) {
        Chat freshChat = CHAT_REPOSITORY.getById(chatID);
        ArrayList<Long> messageIDs = new ArrayList<>();
        List<Message> messages = freshChat.getMessages().stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
        for (Message message : messages) {
            if (!message.isDeleted())
                messageIDs.add(message.getId());
        }
        return messageIDs;
    }

    public void createGroupChat(List<String> members, String name) {
        members.add(LoggedUser.getLoggedUser().getUsername());
        List<User> groupMembers = members.stream().map(USER_REPOSITORY::getByUsername).collect(Collectors.toList());
        Chat chat = new Chat(groupMembers, name);
        CHAT_REPOSITORY.insert(chat);
        log.info(" group chat " + chat.getId() + " was created by " + LoggedUser.getLoggedUser().getUsername());
    }

    public void addMemberToGroupChat(String member, long chatId) {
        Chat chat = CHAT_REPOSITORY.getById(chatId);
        if (!chat.getUserChats().stream().anyMatch(it -> it.getUser().getUsername().equals(member)))
            CHAT_REPOSITORY.addMemberToGroupChat(chatId, new UserChat(USER_REPOSITORY.getByUsername(member), chat));
    }

    public void addNewMessageToGroupChat(String message, byte[] image, long chatId) {
        Message newMessage = new Message(message, image, USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()));
        CHAT_REPOSITORY.addMessageToChat(chatId, newMessage);
    }

    public String getChatName(Long chatId) {
        Chat chat = CHAT_REPOSITORY.getById(chatId);
        if (chat.isGroup()) {
            return chat.getName();
        } else {
            return getFrontUser(chatId).getUsername();
        }
    }

    public String getUnseenCount(Long chatId) {
        Chat chat = CHAT_REPOSITORY.getById(chatId);
        UserChat userToSee = chat.getUserChats().stream().filter(it -> it.getUser().getId() == LoggedUser.getLoggedUser().getId()).findAny().orElseThrow();
        return String.valueOf(userToSee.getUnseenCount());
    }

    public boolean isGroup(Long chatId) {
        Chat chat = CHAT_REPOSITORY.getById(chatId);
        return chat.isGroup();
    }

    public ArrayList<String> getMembersNames(long groupId) {
        Chat chat = CHAT_REPOSITORY.getById(groupId);
        List<UserChat> userChats = chat.getUserChats();
        ArrayList<String> names = new ArrayList<>();
        for (UserChat userChat : userChats) {
            names.add(userChat.getUser().getUsername());
        }
        return names;
    }

    public void leaveGroup(long groupId) {
        CHAT_REPOSITORY.leaveGroup(LoggedUser.getLoggedUser().getId() , groupId);
    }
}