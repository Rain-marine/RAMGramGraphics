package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NotificationController implements Repository {
    private final static Logger log = LogManager.getLogger(NotificationController.class);

    public NotificationController() {
    }

    public void sendFollowRequestToUser(long userId) {
        User receiver = USER_REPOSITORY.getById(userId);
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        Notification followRequestNotification = new Notification(loggedUser, receiver, NotificationType.FOLLOW_REQ);
        NOTIFICATION_REPOSITORY.insert(followRequestNotification);
        log.info( LoggedUser.getLoggedUser().getUsername() + " requested " + userId );
    }

    public void FollowUser(long userId) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User receiver = USER_REPOSITORY.getById(userId);
        if (loggedUser.getFollowings().stream().noneMatch(it -> it.getId() == receiver.getId())) {
            Notification followNotification = new Notification(loggedUser, receiver, NotificationType.START_FOLLOW);
            NOTIFICATION_REPOSITORY.insert(followNotification);
            NOTIFICATION_REPOSITORY.addNewFollower(receiver.getId(), loggedUser.getId());
            //notificationRepository.addNewFollowing(loggedUser.getId(), receiver.getId());
            log.info( LoggedUser.getLoggedUser().getUsername() + " followed " + userId );
        }


    }

    public void unfollowUserWithNotification(long userId) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User receiver = USER_REPOSITORY.getById(userId);
        Notification unfollowNotification = new Notification(loggedUser, receiver, NotificationType.UNFOLLOW);

        NOTIFICATION_REPOSITORY.insert(unfollowNotification);
        NOTIFICATION_REPOSITORY.removeFromFollowings(loggedUser.getId(), receiver.getId());
        NOTIFICATION_REPOSITORY.removeFromFollowers(receiver.getId(), loggedUser.getId());

        for (Group group : USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId()).getGroups()) {
            for (User member : group.getMembers()) {
                if (member.getUsername().equals(receiver.getUsername())) {
                    FACTION_REPOSITORY.removeUserFromGroup(receiver.getId(), group.getId());
                    break;
                }
            }
        }
        log.info( LoggedUser.getLoggedUser().getUsername() + " unfollowed with notification " + userId );
    }

    public void unfollowUserWithoutNotification(long userId) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());

        NOTIFICATION_REPOSITORY.removeFromFollowings(loggedUser.getId(), userId);
        NOTIFICATION_REPOSITORY.removeFromFollowers(userId, loggedUser.getId());

        List<Group> loggedUserGroups = USER_REPOSITORY.getById(loggedUser.getId()).getGroups();
        for (Group group : loggedUserGroups) {
            for (User member : group.getMembers()) {
                if (member.getUsername().equals(USER_REPOSITORY.getById(userId).getUsername())) {
                    FACTION_REPOSITORY.removeUserFromGroup(userId, group.getId());
                    break;
                }
            }
        }
        log.info( LoggedUser.getLoggedUser().getUsername() + " unfollowed without notification " + userId );
    }

    public List<Notification> getFollowingRequestsNotifications() {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Notification> notifications = user.getReceiverNotifications();
        List<Notification> followNotification = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getReceiver().getUsername().equals(LoggedUser.getLoggedUser().getUsername())) {
                if (notification.getType() == NotificationType.FOLLOW_REQ)
                    followNotification.add(notification);
            }
        }
        return followNotification;
    }

    public List<Notification> getYourFollowingRequestNotification() {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Notification> notifications = user.getSenderNotifications();
        List<Notification> followNotification = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getSender().getUsername().equals(LoggedUser.getLoggedUser().getUsername())) {
                if (notification.getType() == NotificationType.FOLLOW_REQ)
                    followNotification.add(notification);
            }
        }
        return followNotification;
    }

    public List<Notification> getSystemNotification() {
        User user = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        List<Notification> notifications = user.getReceiverNotifications();
        List<Notification> followNotification = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getReceiver().getUsername().equals(LoggedUser.getLoggedUser().getUsername())) {
                if (notification.getType() != NotificationType.FOLLOW_REQ)
                    followNotification.add(notification);
            }
        }
        return followNotification;
    }

    public void acceptFollowRequest(Notification notification) {
        deleteNotification(notification);

        NOTIFICATION_REPOSITORY.addNewFollower(LoggedUser.getLoggedUser().getId(), notification.getSender().getId());
        //notificationRepository.addNewFollowing(notification.getSender().getId(), LoggedUser.getLoggedUser().getId());
        log.info( LoggedUser.getLoggedUser().getUsername() + " accepted " + notification.getSender().getUsername() );
    }

    public void rejectFollowRequestWithNotification(Notification notification) {
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        User requestSender = USER_REPOSITORY.getById(notification.getSender().getId());
        Notification rejectFollowRequest = new Notification(loggedUser, requestSender, NotificationType.FOLLOW_REQ_REJECT);
        NOTIFICATION_REPOSITORY.insert(rejectFollowRequest);

        deleteNotification(notification);
    }

    public void rejectFollowRequestWithoutNotification(Notification notification) {
        deleteNotification(notification);
    }

    public void deleteNotification(Notification notification) {
        NOTIFICATION_REPOSITORY.deleteNotification(notification.getId());
        User loggedUser = USER_REPOSITORY.getById(LoggedUser.getLoggedUser().getId());
        loggedUser.getReceiverNotifications().remove(notification);
    }

    public void deleteRequest(long rawUserId) {
        User user = USER_REPOSITORY.getById(rawUserId);
        long loggedUserId = LoggedUser.getLoggedUser().getId();
        Notification request = user.getReceiverNotifications().stream()
                .filter(it -> ((it.getSender().getId() == loggedUserId) && (it.getType() == NotificationType.FOLLOW_REQ)))
                .collect(Collectors.toList()).get(0);
        NOTIFICATION_REPOSITORY.deleteNotification(request.getId());
    }
}