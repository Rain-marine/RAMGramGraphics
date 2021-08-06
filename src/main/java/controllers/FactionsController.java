package controllers;

import models.Group;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class FactionsController implements Repository {

    public FactionsController() {
    }

    public void insertNewFaction(String name, List<String> users) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        Group newGroup = new Group(name, loggedUser);

        List<User> members = new ArrayList<>();
        for (String username : users) {
            if (members.stream().noneMatch(it -> it.getUsername().equals(username)))
                members.add(USER_REPOSITORY.getByUsername(username));
        }
        newGroup.setMembers(members);
        FACTION_REPOSITORY.insert(newGroup);
    }

    public boolean canAddToGroup(String username) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
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
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        return loggedUser.getGroups();
    }

    public List<User> getActiveFollowers() {
        List<User> followers = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getFollowers();
        List<User> activeFollowers = new ArrayList<>();
        followers.forEach(following -> {
            if (following.isActive())
                activeFollowers.add(following);
        });
        return activeFollowers;
    }

    public List<User> getActiveFollowings() {
        List<User> followings = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getFollowings();
        List<User> activeFollowings = new ArrayList<>();
        followings.forEach(following -> {
            if (following.isActive())
                activeFollowings.add(following);
        });
        return activeFollowings;
    }

    public List<User> getActiveBlockedUsers() {
        List<User> blockedUsers = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getBlackList();
        List<User> activeBlockedUsers = new ArrayList<>();
        blockedUsers.forEach(following -> {
            if(following.isActive())
                activeBlockedUsers.add(following);
        });
        return activeBlockedUsers;
    }

    public List<User> getGroupMembers(int factionId) {
        Group faction = FACTION_REPOSITORY.getFactionById(factionId);
        return faction.getMembers();
    }

    public void deleteFaction(int groupId) {
        FACTION_REPOSITORY.deleteFaction(groupId);
        USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getGroups().remove(FACTION_REPOSITORY.getFactionById(groupId));
        //LoggedUser.setLoggedUser(userRepository.getById(LoggedUser.getLoggedUser().getId()));
    }

    public void deleteUserFromFaction(int factionId, long userId) {
        FACTION_REPOSITORY.deleteUserFromFaction(factionId, userId);
    }

    public void addUserToFaction(int factionId, String username) {
        FACTION_REPOSITORY.addUserToFaction(factionId, USER_REPOSITORY.getByUsername(username).getId());
    }

    public ArrayList<String> getFactionNames() {
        ArrayList<String> factionNames = new ArrayList<>();
        List<Group> factions = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getGroups();
        for (Group faction : factions) {
            factionNames.add(faction.getName());
        }
        return factionNames;
    }
}