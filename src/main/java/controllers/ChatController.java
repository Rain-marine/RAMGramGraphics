package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.ChatRepository;
import repository.UserRepository;

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

    public List<Chat> getChats() {
        return chatRepository.getAllChats(LoggedUser.getLoggedUser().getId());
    }

    public void seeChat(Chat chat) {
        chatRepository.clearUnSeenCount(chat.getId(), LoggedUser.getLoggedUser().getId());
    }

    public void addMessageToChat(long chatId, String message,byte[] images, User frontUser) {
        Message newMessage = new Message(message,images,
                userRepository.getById(LoggedUser.getLoggedUser().getId()),
                userRepository.getById(frontUser.getId()));
        chatRepository.addMessageToChat(chatId, newMessage);
    }

    public List<Message> getMessages(Chat chat) {
        Chat freshChat = chatRepository.getById(chat.getId());
        return freshChat.getMessages().stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
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
}