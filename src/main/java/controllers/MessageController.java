package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageController implements Repository{
    private final static Logger log = LogManager.getLogger(MessageController.class);

    public MessageController() {
    }

    public ArrayList<Long> getSavedMessage() {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Message> messages = user.getFavoriteMessages();
        ArrayList<Long> messageIDs = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isDeleted())
                messageIDs.add(message.getId());
        }
        return messageIDs;
    }

    public ArrayList<Long> getSavedTweets() {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Tweet> tweets = user.getFavoriteTweets();
        ArrayList<Long> tweetIDs = new ArrayList<>();
        for (Tweet tweet : tweets) {
            tweetIDs.add(tweet.getId());
        }
        return tweetIDs;
    }

    public boolean canSendMessageToUser(String userToSendMessage) {
        User user = USER_REPOSITORY.getByUsername(userToSendMessage);
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
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        for (Group group : user.getGroups()) {
            if (group.getName().equals(groupToSendMessage))
                return true;
        }
        return false;
    }

    public void sendMessage(String message, byte[] image, List<String> users, List<String> groupsToSendMessage) {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Chat> userChats = user.getChats();
        List<Group> groups = user.getGroups();
        HashMap<String, Group> groupNameToGroup = extractGroupNameToGroup(groups);

        sendMessageToUsers(message, image, users, userChats);
        sendMessageToGroups(message, image, groupsToSendMessage, groupNameToGroup, userChats);
    }


    private void sendMessageToGroups(String message, byte[] image, List<String> groupsToSendMessage, HashMap<String, Group> groups, List<Chat> chats) {
        for (String groupName : groupsToSendMessage) {
            List<String> users = new ArrayList<>();
            Group faction = FACTION_REPOSITORY.getFactionById(groups.get(groupName).getId());
            faction.getMembers().forEach(member -> users.add(member.getUsername()));
            sendMessageToUsers(message, image, users, chats);
        }
    }

    private void sendMessageToUsers(String message, byte[] image, List<String> users, List<Chat> chats) {
        for (String user : users) {
            boolean hasSent = false;
            User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
            User receiver = USER_REPOSITORY.getByUsername(user);
            Message newMessage = new Message(message, image, loggedUser, receiver);
            for (Chat chat : chats) {
                if (chat.getUserChats().size() == 2 &&
                        (chat.getUserChats().get(0).getUser().getUsername().equals(user)
                                || chat.getUserChats().get(1).getUser().getUsername().equals(user))) {
                    CHAT_REPOSITORY.addMessageToChat(chat.getId(), newMessage);
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
                CHAT_REPOSITORY.insert(newChat);
                Chat chat = getChatWithId(receiver.getId());
                CHAT_REPOSITORY.addMessageToChat(chat.getId(), newMessage);
            }
        }
    }

    public void insertSavedMessage(long messageId) {
        MESSAGE_REPOSITORY.addMessageToSavedMessage(LoggedUser.getLoggedUser().getId(), MESSAGE_REPOSITORY.getById(messageId));
    }

    private HashMap<String, Group> extractGroupNameToGroup(List<Group> groups) {
        HashMap<String, Group> groupNameToGroup = new HashMap<>();
        groups.forEach(group -> groupNameToGroup.put(group.getName(), group));

        return groupNameToGroup;
    }

    public void forwardTweet(long tweetId , String receiver) {
        Tweet tweet = TWEET_REPOSITORY.getById(tweetId);
        String message = "Tweet forwarded from " + tweet.getUser().getUsername() + "\n" + tweet.getText();
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User receiveUser = USER_REPOSITORY.getByUsername(receiver);
        Message newMessage = new Message(message, tweet.getImage(), loggedUser, receiveUser);
        newMessage.setGrandSender(tweet.getUser());

        for (Chat chat : loggedUser.getChats()) {
            if (chat.getUserChats().size() == 2 &&
                    (chat.getUserChats().get(0).getUser().getUsername().equals(receiver)
                            || chat.getUserChats().get(1).getUser().getUsername().equals(receiver))) {
                CHAT_REPOSITORY.addMessageToChat(chat.getId(), newMessage);
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
        CHAT_REPOSITORY.insert(newChat);
    }

    public Chat getChatWithId(long userId) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User receiveUser = USER_REPOSITORY.getById(userId);
        String username = receiveUser.getUsername();
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
        CHAT_REPOSITORY.insert(newChat);
        return null;
    }

    public void deleteMessage(long id) {
        MESSAGE_REPOSITORY.delete(id);
        log.info("message " + id + " was deleted");
    }

    public void editMessage(long messageId, String newText) {
        MESSAGE_REPOSITORY.editMessageText(messageId, newText);
    }

    public byte[] getMessageImage(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getImage();
    }

    public String getMessageText(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getText();
    }

    public String getMessageDate(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getDate().toString();
    }

    public String getMessageSender(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getSender().getUsername();
    }

    public String getMessageGrandSender(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getGrandSender().getUsername();
    }

    public byte[] getSenderProfile(long messageId) {
        return MESSAGE_REPOSITORY.getById(messageId).getSender().getProfilePhoto();
    }

    public void forward(long messageID, List<String> users, List<String> factions) {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Chat> userChats = user.getChats();
        List<Group> groups = user.getGroups();
        HashMap<String, Group> groupNameToGroup = extractGroupNameToGroup(groups);

        forwardMessageToUsers(messageID, users, userChats);
        forwardMessageToFactions(messageID, factions, groupNameToGroup, userChats);
    }

    private void forwardMessageToFactions(long messageID, List<String> factions, HashMap<String, Group> groupNameToGroup, List<Chat> chats) {
        for (String groupName : factions) {
            List<String> users = new ArrayList<>();
            Group faction = FACTION_REPOSITORY.getFactionById(groupNameToGroup.get(groupName).getId());
            faction.getMembers().forEach(member -> users.add(member.getUsername()));
            forwardMessageToUsers(messageID, users, chats);
        }
    }

    private void forwardMessageToUsers(long messageID, List<String> users, List<Chat> chats) {
        Message message = MESSAGE_REPOSITORY.getById(messageID);
        for (String user : users) {
            boolean hasSent = false;
            User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
            User receiver = USER_REPOSITORY.getByUsername(user);
            Message newMessage = new Message(message.getText(), message.getImage(), loggedUser, receiver);
            newMessage.setGrandSender(message.getSender());
            for (Chat chat : chats) {
                chat = CHAT_REPOSITORY.getById(chat.getId());
                if (chat.getUserChats().size() == 2 &&
                        (chat.getUserChats().get(0).getUser().getUsername().equals(user)
                                || chat.getUserChats().get(1).getUser().getUsername().equals(user))) {
                    CHAT_REPOSITORY.addMessageToChat(chat.getId(), newMessage);
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
                CHAT_REPOSITORY.insert(newChat);
                Chat chat = getChatWithId(receiver.getId());
                CHAT_REPOSITORY.addMessageToChat(chat.getId(), newMessage);
            }
        }
    }

    public void addSavedMessage(String messageText, byte[] chosenImageByteArray) {
        Message message = new Message(messageText, chosenImageByteArray, LoggedUser.getLoggedUser());
        MESSAGE_REPOSITORY.addMessageToSavedMessage(LoggedUser.getLoggedUser().getId(), message);
    }

    public long getChatWithUser(long userId) {
        Chat chat = getChatWithId(userId);
        if (chat == null)
            chat = getChatWithId(userId);
        return chat.getId();
    }


    public enum TYPE {EDIT, DELETE, BOTH, NONE}

    public TYPE getMessageType(long messageId) {
        Message message = MESSAGE_REPOSITORY.getById(messageId);
        boolean editable = (message.getSender().getId() == message.getGrandSender().getId()
                && message.getSender().getId() == LoggedUser.getLoggedUser().getId());
        boolean removable = (message.getSender().getId() == LoggedUser.getLoggedUser().getId());
        if (editable && removable) {
            return TYPE.BOTH;
        } else if (editable) {
            return TYPE.EDIT;
        } else if (removable) {
            return TYPE.DELETE;
        } else
            return TYPE.NONE;
    }
}