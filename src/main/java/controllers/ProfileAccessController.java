package controllers;

import gui.controllers.profiles.*;
import models.LoggedUser;
import models.NotificationType;
import models.User;
import repository.UserRepository;
import java.util.List;

public class ProfileAccessController {

    private final User loggedUser;
    private final long loggedUserId;
    private final User otherUser;
    private final int previousMenu;
    private final long otherUserId;
    private final UserRepository userRepository;
    private int factionId;

    public ProfileAccessController(int previousMenu, User otherUser1 , int factionId ) {
        this.userRepository = new UserRepository();
        this.loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        this.otherUser = userRepository.getById(otherUser1.getId());
        this.previousMenu = previousMenu;
        this.loggedUserId = loggedUser.getId();
        this.otherUserId = otherUser.getId();
        this.factionId = factionId;
    }

    public String checkAccessibility() {
        //is it myself? :)
        if(otherUserId==loggedUserId)
            return "FXMLs/PersonalPage/PersonalPageMenu.fxml";
        //is Active>
        if (!otherUser.isActive()){
            return "FXMLs/Profiles/NotVisibleProfile.fxml";
        }
        else {
            //have I blocked them?
            List<User> loggedUserBlacklist = loggedUser.getBlackList();
            for (User user : loggedUserBlacklist) {
                if (user.getId() == otherUserId) {
                    BlockedProfileGuiController.setUser(otherUser);
                    BlockedProfileGuiController.setPrevious(previousMenu);
                    BlockedProfileGuiController.setFactionId(factionId);
                    BlockedProfileGuiController.setProfileAccessController(this);
                    return "FXMLs/Profiles/BlockedProfile.fxml";
                }
            }
            //am I following them?
            List<User> loggedUserFollowing = loggedUser.getFollowings();
            for (User user : loggedUserFollowing) {
                if (user.getId() == otherUserId) {
                    FollowingProfileGuiController.setUser(otherUser);
                    FollowingProfileGuiController.setPrevious(previousMenu);
                    FollowingProfileGuiController.setFactionId(factionId);
                    FollowingProfileGuiController.setProfileAccessController(this);
                    return "FXMLs/Profiles/FollowingProfile.fxml";
                }
            }
            //am I blocked?
            List<User> blackList =otherUser.getBlackList();
            for (User user : blackList) {
                if (user.getId() == loggedUserId){
                    return "FXMLs/Profiles/NotVisibleProfile.fxml";
                }
            }
            //is their account private?
            if (otherUser.isPublic()){
                PublicProfileGuiController.setUser(otherUser);
                PublicProfileGuiController.setPrevious(previousMenu);
                PublicProfileGuiController.setProfileAccessController(this);
                return "FXMLs/Profiles/PublicProfile.fxml";
            }

            //have I send request?
            if (otherUser.getReceiverNotifications().stream().anyMatch(it -> ((it.getSender().getId() == loggedUserId)
            && (it.getType() == NotificationType.FOLLOW_REQ)))) {
                PendingRequestProfileGuiController.setPrevious(previousMenu);
                PendingRequestProfileGuiController.setUser(otherUser);
                return "FXMLs/Profiles/PendingRequestProfile.fxml";

            }
            PrivateProfileGuiController.setUser(otherUser);
            PrivateProfileGuiController.setPrevious(previousMenu);
            return "FXMLs/Profiles/PrivateProfile.fxml";



        }
    }

    public String checkFollowing() {
        //Active, public, not block,
        //am I following them?
        List<User> loggedUserFollowing = loggedUser.getFollowings();
        for (User user : loggedUserFollowing) {
            if (user.getId() == otherUserId) {
                FollowingProfileGuiController.setUser(otherUser);
                FollowingProfileGuiController.setPrevious(previousMenu);
                FollowingProfileGuiController.setFactionId(factionId);
                FollowingProfileGuiController.setProfileAccessController(this);
                return "FXMLs/Profiles/FollowingProfile.fxml";
            }
        }
        //todo: previous
        return "FXMLs/Profiles/PublicProfile.fxml";
    }
}
