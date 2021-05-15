package controllers;

import models.LoggedUser;
import models.NotificationType;
import models.User;
import repository.UserRepository;
import views.Menu;
import views.profiles.*;

import java.util.List;

public class ProfileAccessController {

    private final User loggedUser;
    private final long loggedUserId;
    private final User otherUser;
    private final Menu previousMenu;
    private final long otherUserId;
    private final UserRepository userRepository;

    public ProfileAccessController(Menu previousMenu, User otherUser1 ) {
        userRepository = new UserRepository();
        this.loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        this.otherUser = userRepository.getById(otherUser1.getId());
        this.previousMenu = previousMenu;        //previous menu may only be explorer
        this.loggedUserId = loggedUser.getId();
        this.otherUserId = otherUser.getId();

    }

    public Menu checkAccessibility() {
        //is it myself? :)
        if(otherUserId==loggedUserId)
            return new SelfProfile(previousMenu);
        //is Active>
        if (!otherUser.isActive()){
            return new ProfileNotVisible();
        }
        else {
            //have I blocked them?
            List<User> loggedUserBlacklist = loggedUser.getBlackList();
            for (User user : loggedUserBlacklist) {
                if (user.getId() == otherUserId) {
                    return new BlockedProfile(otherUser, previousMenu);
                }
            }
            //am I following them?
            List<User> loggedUserFollowing = loggedUser.getFollowings();
            for (User user : loggedUserFollowing) {
                if (user.getId() == otherUserId) {
                    return new FollowingProfile(otherUser, previousMenu);
                }
            }
            //am I blocked?
            List<User> blackList =otherUser.getBlackList();
            for (User user : blackList) {
                if (user.getId() == loggedUserId){
                    return new ProfileNotVisible();
                }
            }
            //is their account private?
            if (otherUser.isPublic()){
                return new PublicProfile(otherUser,previousMenu);
            }

            //have I send request?
            if (otherUser.getReceiverNotifications().stream().anyMatch(it -> ((it.getSender().getId() == loggedUserId)
            && (it.getType() == NotificationType.FOLLOW_REQ)))) {
                return new PendingRequestProfile(otherUser,previousMenu);

            }
            return new PrivateProfile(otherUser, previousMenu);



        }
    }

    public Menu checkFollowing() {
        //Active, public, not block,
        //am I following them?
        List<User> loggedUserFollowing = loggedUser.getFollowings();
        for (User user : loggedUserFollowing) {
            if (user.getId() == otherUserId) {
                return new FollowingProfile(otherUser, previousMenu);
            }
        }
        return new PublicProfile(otherUser,previousMenu);
    }
}
