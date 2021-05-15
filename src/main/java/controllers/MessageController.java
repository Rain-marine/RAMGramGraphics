package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.ChatRepository;
import repository.MessageRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageController {
    private final static Logger log = LogManager.getLogger(MessageController.class);
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageController() {
        messageRepository = new MessageRepository();
        userRepository = new UserRepository();
        chatRepository = new ChatRepository();
    }

    public List<Message> getSavedMessage(){
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
        return user.getFavoriteMessages();
    }

    public List<Tweet> getSavedTweets() {
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
        return user.getFavoriteTweets();
    }

    public boolean canSendMessageToUser(String userToSendMessage) {
        User user = userRepository.getByUsername(userToSendMessage);
        if (user == null || !user.isActive())
            return false;
        if (hasFollow(user.getFollowers()))
            return true;
        return isFollower(user.getFollowings());
    }

    private boolean isFollower(List<User> followings) {
        for (User user : followings) {
            if (user.getUsername().equals(LoggedUser.getLoggedUser().getUsername()))
                return true;
        }
        return false;
    }

    private boolean hasFollow(List<User> followers) {
        for (User user : followers) {
            if (user.getUsername().equals(LoggedUser.getLoggedUser().getUsername()))
                return true;
        }
        return false;
    }

    public boolean canSendMessageToGroup(String groupToSendMessage) {
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
        for (Group group : user.getGroups()) {
            if (group.getName().equals(groupToSendMessage))
                return true;
        }
        return false;
    }

    public void sendMessage(String message, byte[] image, List<String> users, List<String> groupsToSendMessage) {
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<Chat> userChats = user.getChats();
        List<Group> groups = user.getGroups();
        HashMap<String, Group> groupNameToGroup = extractGroupNameToGroup(groups);

        sendMessageToUsers(message,image, users, userChats);
        sendMessageToGroups(message, image, groupsToSendMessage, groupNameToGroup, userChats);
    }

    private void sendMessageToGroups(String message, byte[] image, List<String> groupsToSendMessage, HashMap<String, Group> groups, List<Chat> chats) {
        for (String groupName : groupsToSendMessage) {
            List<String> users = new ArrayList<>();
            groups.get(groupName).getMembers().forEach(member -> users.add(member.getUsername()));
            sendMessageToUsers(message, image, users, chats);
        }
    }

    private void sendMessageToUsers(String message, byte[] image, List<String> users, List<Chat> chats) {
        for (String user : users) {
            boolean hasSent = false;
            User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
            User receiver = userRepository.getByUsername(user);
            Message newMessage = new Message(message, image, loggedUser, receiver);
            for (Chat chat : chats) {
                if (chat.getUserChats().size() == 2 &&
                        (chat.getUserChats().get(0).getUser().getUsername().equals(user)
                                || chat.getUserChats().get(1).getUser().getUsername().equals(user))) {
                    chatRepository.addMessageToChat(chat.getId(), newMessage);
                    hasSent = true;
                    break;
                }
            }
            if (!hasSent) {
                Chat newChat = new Chat(new ArrayList<>() {
                    {
                        add(loggedUser);
                        add(receiver);
                    }
                });
                chatRepository.insert(newChat);
                Chat chat = getChatWithUsername(receiver.getUsername());
                chatRepository.addMessageToChat(chat.getId(),newMessage);
            }
        }
    }

    public void insertSavedMessage(Message message) {
        messageRepository.addMessageToSavedMessage(LoggedUser.getLoggedUser().getId(), message);
    }

    private HashMap<String, Group> extractGroupNameToGroup(List<Group> groups) {
        HashMap<String, Group> groupNameToGroup = new HashMap<>();
        groups.forEach(group -> groupNameToGroup.put(group.getName(), group));

        return groupNameToGroup;
    }

    public void forwardTweet(Tweet tweet, User tweetUser, String receiver) {
        String message = "Tweet forwarded from " + tweetUser.getUsername() + "\n" + tweet.getText();
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        User receiveUser = userRepository.getByUsername(receiver);
        Message newMessage = new Message(message, null, loggedUser, receiveUser);
        newMessage.setGrandSender(tweetUser);

        for (Chat chat : loggedUser.getChats()) {
            if (chat.getUserChats().size() == 2 &&
                    (chat.getUserChats().get(0).getUser().getUsername().equals(receiver)
                            || chat.getUserChats().get(1).getUser().getUsername().equals(receiver))) {
                chatRepository.addMessageToChat(chat.getId(), newMessage);
                return;
            }
        }
        Chat newChat = new Chat(new ArrayList<>() {
            {
                add(loggedUser);
                add(receiveUser);
            }
        });
        newChat.setMessages(new ArrayList<>() {
            {
                add(newMessage);
            }
        });
        newChat.getUserChats().get(1).setHasSeen(false);
        newChat.getUserChats().get(1).setUnseenCount(1);
        chatRepository.insert(newChat);
    }

    public Chat getChatWithUsername(String username) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        User receiveUser = userRepository.getByUsername(username);
        for (Chat chat : loggedUser.getChats()) {
            if (chat.getUserChats().size() == 2 &&
                    (chat.getUserChats().get(0).getUser().getUsername().equals(username)
                            || chat.getUserChats().get(1).getUser().getUsername().equals(username)))
                return chat;
        }

        Chat newChat = new Chat(new ArrayList<>() {
            {
                add(loggedUser);
                add(receiveUser);
            }
        });
        newChat.setMessages(new ArrayList<>());
        chatRepository.insert(newChat);
        return null;
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message.getId());
    }

    public void editMessage(Message message, String newText) {
        messageRepository.editMessageText(message.getId(), newText);
    }
}