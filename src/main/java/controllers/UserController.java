package controllers;

import models.Group;
import models.LoggedUser;
import models.User;
import repository.FactionRepository;
import repository.NotificationRepository;
import repository.UserRepository;

import java.util.Date;
import java.util.List;

public class UserController {
    private final UserRepository userRepository;
    private final FactionRepository factionRepository;
    private final NotificationRepository notificationRepository;

    public UserController() {
        this.userRepository = new UserRepository();
        factionRepository = new FactionRepository();
        notificationRepository = new NotificationRepository();
    }

    public void blockUser(User userToBlock) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        for (User user : loggedUser.getFollowers())
            if(user.getUsername().equals(userToBlock.getUsername())) {
                notificationRepository.removeFromFollowers(loggedUser.getId(), user.getId());
                break;
            }
        for (User user : userRepository.getById(LoggedUser.getLoggedUser().getId()).getFollowings())
            if(user.getUsername().equals(userToBlock.getUsername())) {
                notificationRepository.removeFromFollowings(loggedUser.getId(), user.getId());
                break;
            }
        for (Group group : userRepository.getById(LoggedUser.getLoggedUser().getId()).getGroups()) {
            for (User member : group.getMembers()) {
                if(member.getUsername().equals(userToBlock.getUsername())) {
                    factionRepository.removeUserFromGroup(member.getId(), group.getId());
                    break;
                }
            }
        }
        factionRepository.addUserToBlackList(loggedUser.getId(), userToBlock.getId());
    }

    public void muteUser(User user) {
        userRepository.mute(LoggedUser.getLoggedUser().getId(), user.getId());
    }

    public User getUserByUsername(String usernameToFind) {
        return userRepository.getByUsername(usernameToFind);
    }

    public void reportUser(User user) {
        userRepository.increaseReportCount(user.getId());
    }

    public void ChangeUsername(String newUsername) {
        userRepository.changeUsername(LoggedUser.getLoggedUser().getId(), newUsername);
    }

    public void changeBio(String newBio) {
        userRepository.changeBio(LoggedUser.getLoggedUser().getId(), newBio);
    }

    public void changeName(String newName) {
        userRepository.changeFullName(LoggedUser.getLoggedUser().getId(), newName);
    }

    public void changeBirthday(Date birthday) {
        userRepository.changeBirthdayDate(LoggedUser.getLoggedUser().getId(), birthday);
    }

    public void changeEmail(String newEmail) {
        userRepository.changeEmail(LoggedUser.getLoggedUser().getId(), newEmail);
    }

    public void changeNumber(String newNumber) {
        userRepository.changePhoneNumber(LoggedUser.getLoggedUser().getId(), newNumber);
    }

    public void unblockUser(User user) {
        userRepository.unblock(LoggedUser.getLoggedUser().getId(), user.getId());
    }

    public boolean isAccountPublic(String username) {
        return userRepository.getByUsername(username).isPublic();
    }

    public void changeProfilePhoto(byte[] newPhoto){
        userRepository.changeProfilePhoto(LoggedUser.getLoggedUser().getId(), newPhoto);
    }
}
