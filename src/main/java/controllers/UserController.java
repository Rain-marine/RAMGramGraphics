package controllers;

import models.Group;
import models.LoggedUser;
import models.User;
import repository.FactionRepository;
import repository.NotificationRepository;
import repository.UserRepository;

import java.util.Date;

public class UserController {
    private final UserRepository USER_REPOSITORY;
    private final FactionRepository FACTION_REPOSITORY;
    private final NotificationRepository NOTIFICATION_REPOSITORY;
    private final RegisterManager REGISTER_MANAGER;

    public UserController() {
        USER_REPOSITORY = new UserRepository();
        FACTION_REPOSITORY = new FactionRepository();
        NOTIFICATION_REPOSITORY = new NotificationRepository();
        REGISTER_MANAGER = new RegisterManager();
    }

    public void blockUser(User userToBlock) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        for (User user : loggedUser.getFollowers())
            if(user.getUsername().equals(userToBlock.getUsername())) {
                NOTIFICATION_REPOSITORY.removeFromFollowers(loggedUser.getId(), user.getId());
                break;
            }
        for (User user : USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getFollowings())
            if(user.getUsername().equals(userToBlock.getUsername())) {
                NOTIFICATION_REPOSITORY.removeFromFollowings(loggedUser.getId(), user.getId());
                break;
            }
        for (Group group : USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getGroups()) {
            for (User member : group.getMembers()) {
                if(member.getUsername().equals(userToBlock.getUsername())) {
                    FACTION_REPOSITORY.removeUserFromGroup(member.getId(), group.getId());
                    break;
                }
            }
        }
        FACTION_REPOSITORY.addUserToBlackList(loggedUser.getId(), userToBlock.getId());
    }

    public void muteUser(User user) {
        USER_REPOSITORY.mute(LoggedUser.getLoggedUser().getId(), user.getId());
    }

    public User getUserByUsername(String usernameToFind) {
        return USER_REPOSITORY.getByUsername(usernameToFind);
    }

    public void reportUser(User user) {
        USER_REPOSITORY.increaseReportCount(user.getId());
    }

    public boolean ChangeUsername(String newUsername) {
        if (REGISTER_MANAGER.isUsernameAvailable(newUsername)) {
            USER_REPOSITORY.changeUsername(LoggedUser.getLoggedUser().getId(), newUsername);
            return true;
        }
        return false;
    }

    public void changeBio(String newBio) {
        USER_REPOSITORY.changeBio(LoggedUser.getLoggedUser().getId(), newBio);
    }

    public void changeName(String newName) {
        USER_REPOSITORY.changeFullName(LoggedUser.getLoggedUser().getId(), newName);
    }

    public void changeBirthday(Date birthday) {
        USER_REPOSITORY.changeBirthdayDate(LoggedUser.getLoggedUser().getId(), birthday);
    }

    public boolean changeEmail(String newEmail) {
        if (REGISTER_MANAGER.isEmailAvailable(newEmail)) {
            USER_REPOSITORY.changeEmail(LoggedUser.getLoggedUser().getId(), newEmail);
            return true;
        }
        return false;
    }

    public boolean changeNumber(String newNumber) {
        if(REGISTER_MANAGER.isPhoneNumberAvailable(newNumber)) {
            USER_REPOSITORY.changePhoneNumber(LoggedUser.getLoggedUser().getId(), newNumber);
            return true;
        }
        return false;
    }

    public void unblockUser(User user) {
        USER_REPOSITORY.unblock(LoggedUser.getLoggedUser().getId(), user.getId());
    }

    public boolean isAccountPublic(String username) {
        return USER_REPOSITORY.getByUsername(username).isPublic();
    }

    public void changeProfilePhoto(byte[] newPhoto){
        USER_REPOSITORY.changeProfilePhoto(LoggedUser.getLoggedUser().getId(), newPhoto);
    }

    public User getById(long id) {
        return USER_REPOSITORY.getById(id);
    }
}
