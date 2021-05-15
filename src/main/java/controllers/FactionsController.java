package controllers;

import models.Group;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.FactionRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FactionsController {
    private final static Logger log = LogManager.getLogger(FactionsController.class);
    private final UserRepository userRepository;
    private final FactionRepository factionRepository;

    public FactionsController() {
        userRepository = new UserRepository();
        factionRepository = new FactionRepository();
    }

    public void insertNewFaction(String name, List<String> users) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        Group newGroup = new Group(name, loggedUser);

        List<User> members = new ArrayList<>();
        for (String username : users) {
            if (members.stream().noneMatch(it -> it.getUsername().equals(username)))
                members.add(userRepository.getByUsername(username));
        }
        newGroup.setMembers(members);
        factionRepository.insert(newGroup);
    }

    public boolean canAddToGroup(String username) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<User> followings = loggedUser.getFollowings();
        for (User user : followings) {
            if(username.equals(user.getUsername())){
                if(user.isActive())
                    return true;
            }
        }
        return false;
    }

    public List<Group> getFactions() {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        return loggedUser.getGroups();
    }

    public List<User> getActiveFollowers() {
        List<User> followers = userRepository.getById(LoggedUser.getLoggedUser().getId()).getFollowers();
        List<User> activeFollowers = new ArrayList<>();
        followers.forEach(following -> {
            if (following.isActive())
                activeFollowers.add(following);
        });
        return activeFollowers;
    }

    public List<User> getActiveFollowings() {
        List<User> followings = userRepository.getById(LoggedUser.getLoggedUser().getId()).getFollowings();
        List<User> activeFollowings = new ArrayList<>();
        followings.forEach(following -> {
            if (following.isActive())
                activeFollowings.add(following);
        });
        return activeFollowings;
    }

    public List<User> getActiveBlockedUsers() {
        List<User> blockedUsers = userRepository.getById(LoggedUser.getLoggedUser().getId()).getBlackList();
        List<User> activeBlockedUsers = new ArrayList<>();
        blockedUsers.forEach(following -> {
            if(following.isActive())
                activeBlockedUsers.add(following);
        });
        return activeBlockedUsers;
    }

    public List<User> getGroupMembers(Group group) {
        Group faction = factionRepository.getFactionById(group.getId());
        return faction.getMembers();
    }

    public void deleteFaction(Group group) {
        factionRepository.deleteFaction(group.getId());
        userRepository.getById(LoggedUser.getLoggedUser().getId()).getGroups().remove(group);
    }

    public void deleteUserFromFaction(Group group, User user) {
        factionRepository.deleteUserFromFaction(group.getId(), user.getId());
    }

    public void addUserToFaction(Group group, String username) {
        factionRepository.addUserToFaction(group.getId(), userRepository.getByUsername(username).getId());
    }
}