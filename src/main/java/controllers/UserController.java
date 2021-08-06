package controllers;

import models.Group;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.Date;

public class UserController implements Repository {
    final RegisterManager REGISTER_MANAGER ;
    private final static Logger log = LogManager.getLogger(UserController.class);


    public UserController() {
        REGISTER_MANAGER = new RegisterManager();
    }

    public void blockUser(long userToBlockId) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User userToBlock = USER_REPOSITORY.getById(userToBlockId);
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

    public void muteUser(long userId) {
        USER_REPOSITORY.mute(LoggedUser.getLoggedUser().getId(), userId);
    }

    public long getUserByUsername(String usernameToFind) throws NullPointerException {
        User user = USER_REPOSITORY.getByUsername(usernameToFind);
        if (user == null){
            throw new NullPointerException();
        }
        return user.getId();
    }

    public void reportUser(long userId) {
        USER_REPOSITORY.increaseReportCount(userId);
        log.warn(userId + " account was reported");
    }

    public boolean ChangeUsername(String newUsername) {
        if (REGISTER_MANAGER.isUsernameAvailable(newUsername)) {
            USER_REPOSITORY.changeUsername(LoggedUser.getLoggedUser().getId(), newUsername);
            log.info(LoggedUser.getLoggedUser().getId() + " user name was changed to "+ newUsername);
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
            log.info(LoggedUser.getLoggedUser().getId() + " email name was changed to "+ newEmail);

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

    public void unblockUser(long userId) {
        USER_REPOSITORY.unblock(LoggedUser.getLoggedUser().getId(), userId);
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

    public String getUsername(long frontUserID) {
        return  USER_REPOSITORY.getById(frontUserID).getUsername();
    }

    public byte[] getProfilePhoto(long frontUserID) {
        return USER_REPOSITORY.getById(frontUserID).getProfilePhoto();

    }

    public String getUserBio(long userId) {
        return USER_REPOSITORY.getById(userId).getBio();
    }

    public String UserFullName(long userId) {
        return USER_REPOSITORY.getById(userId).getFullName();
    }
}
