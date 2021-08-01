package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.ChatRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatController {
    private final static Logger log = LogManager.getLogger(ChatController.class);
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatController() {
        chatRepository = new ChatRepository();
        userRepository = new UserRepository();
    }

    public List<Long> getChatsIds() {
        List<Chat> chats = chatRepository.getAllChats(LoggedUser.getLoggedUser().getId());
        List<Long> chatIds = new ArrayList<>();
        for (Chat chat : chats) {
            chatIds.add(chat.getId());
        }
        return chatIds;
    }

    public void seeChat(long chatId) {
        chatRepository.clearUnSeenCount(chatId, LoggedUser.getLoggedUser().getId());
    }

    public void addMessageToChat(long chatId, String message,byte[] images) {
        long frontUserId = getFrontUserId(chatId);
        Message newMessage = new Message(message,images,
                userRepository.getById(LoggedUser.getLoggedUser().getId()),
                userRepository.getById(frontUserId));
        chatRepository.addMessageToChat(chatId, newMessage);
    }

    public long getFrontUserId(long chatId) {
        User frontUser = getFrontUser(chatId);
        return frontUser.getId();
    }

    private User getFrontUser(long chatId) {
        Chat chat = chatRepository.getById(chatId);
        User frontUser =  chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                ? chat.getUserChats().get(1).getUser()
                : chat.getUserChats().get(0).getUser();
        return frontUser;
    }

    public ArrayList<Long> getMessages(long chatID) {
        Chat freshChat = chatRepository.getById(chatID);
        ArrayList<Long> messageIDs = new ArrayList<>();
        List<Message> messages = freshChat.getMessages().stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
        for (Message message : messages) {
            messageIDs.add(message.getId());
        }
        return messageIDs;
    }

    public void createGroupChat(List<String> members, String name) {
        List<User> groupMembers = members.stream().map(userRepository::getByUsername).collect(Collectors.toList());
        Chat chat = new Chat(groupMembers, name);
        chatRepository.insert(chat);
    }

    public void addMembersToGroupChat(List<String> members, long chatId) {
        Chat chat = chatRepository.getById(chatId);
        members.forEach(it -> chatRepository.addMemberToGroupChat(chatId, new UserChat(userRepository.getByUsername(it), chat)));
    }

    public void addNewMessageToGroupChat(String message,byte[] image, long chatId){
        Message newMessage = new Message(message, image, userRepository.getById(LoggedUser.getLoggedUser().getId()));
        chatRepository.addMessageToChat(chatId, newMessage);
    }

    public String getChatName(Long chatId) {
        Chat chat = chatRepository.getById(chatId);
        if (chat.isGroup()){
            return chat.getName();
        }
        else {
            return getFrontUser(chatId).getUsername();
        }
    }

    public String getUnseenCount(Long chatId) {
        Chat chat = chatRepository.getById(chatId);
        UserChat userToSee = chat.getUserChats().get(0).getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())
                ? chat.getUserChats().get(0)
                : chat.getUserChats().get(1);
        return String.valueOf(userToSee.getUnseenCount());
    }

    public boolean isGroup(Long chatId) {
        Chat chat = chatRepository.getById(chatId);
        return chat.isGroup();
    }
}