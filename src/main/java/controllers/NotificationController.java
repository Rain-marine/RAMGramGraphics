package controllers;

import models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.FactionRepository;
import repository.NotificationRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NotificationController {
    private final static Logger log = LogManager.getLogger(NotificationController.class);
    private final NotificationRepository notificationRepository;
    private final FactionRepository factionRepository;
    private final UserRepository userRepository;


    public NotificationController() {
        notificationRepository = new NotificationRepository();
        userRepository = new UserRepository();
        factionRepository = new FactionRepository();
    }

    public void sendFollowRequestToUser(long userId) {
        User receiver = userRepository.getById(userId);
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        Notification followRequestNotification = new Notification(loggedUser,receiver, NotificationType.FOLLOW_REQ);
        notificationRepository.insert(followRequestNotification);
    }

    public void FollowUser(long userId) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        User receiver = userRepository.getById(userId);
        if(loggedUser.getFollowings().stream().noneMatch(it -> it.getId() == receiver.getId())) {
            Notification followNotification = new Notification(loggedUser, receiver, NotificationType.START_FOLLOW);
            notificationRepository.insert(followNotification);
            notificationRepository.addNewFollower(receiver.getId(), loggedUser.getId());
            //notificationRepository.addNewFollowing(loggedUser.getId(), receiver.getId());
        }


    }

    public void unfollowUserWithNotification(long userId) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        User receiver = userRepository.getById(userId);
        Notification unfollowNotification = new Notification(loggedUser,receiver, NotificationType.UNFOLLOW);

        notificationRepository.insert(unfollowNotification);
        notificationRepository.removeFromFollowings(loggedUser.getId(), receiver.getId());
        notificationRepository.removeFromFollowers(receiver.getId(), loggedUser.getId());

        for (Group group : userRepository.getById(LoggedUser.getLoggedUser().getId()).getGroups()) {
            for (User member : group.getMembers()) {
                if (member.getUsername().equals(receiver.getUsername())) {
                    factionRepository.removeUserFromGroup(receiver.getId(), group.getId());
                    break;
                }
            }
        }
    }
    public void unfollowUserWithoutNotification(long userId) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());

        notificationRepository.removeFromFollowings(loggedUser.getId(), userId);
        notificationRepository.removeFromFollowers(userId, loggedUser.getId());

        List<Group> loggedUserGroups = userRepository.getById(loggedUser.getId()).getGroups();
        for (Group group : loggedUserGroups) {
            for (User member : group.getMembers()) {
                if (member.getUsername().equals(userRepository.getById(userId).getUsername())) {
                    factionRepository.removeUserFromGroup(userId, group.getId());
                    break;
                }
            }
        }
    }

    public List<Notification> getFollowingRequestsNotifications() {
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
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
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
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
        User user = userRepository.getById(LoggedUser.getLoggedUser().getId());
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

        notificationRepository.addNewFollower(LoggedUser.getLoggedUser().getId(), notification.getSender().getId());
        //notificationRepository.addNewFollowing(notification.getSender().getId(), LoggedUser.getLoggedUser().getId());
    }

    public void rejectFollowRequestWithNotification(Notification notification) {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        User requestSender = userRepository.getById(notification.getSender().getId());
        Notification rejectFollowRequest = new Notification(loggedUser, requestSender, NotificationType.FOLLOW_REQ_REJECT);
        notificationRepository.insert(rejectFollowRequest);

        deleteNotification(notification);
    }

    public void rejectFollowRequestWithoutNotification(Notification notification) {
        deleteNotification(notification);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.deleteNotification(notification.getId());
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        loggedUser.getReceiverNotifications().remove(notification);
    }

    public void deleteRequest(long rawUserId) {
        User user = userRepository.getById(rawUserId);
        long loggedUserId = LoggedUser.getLoggedUser().getId();
        Notification request = user.getReceiverNotifications().stream()
                .filter(it -> ((it.getSender().getId() == loggedUserId) && (it.getType() == NotificationType.FOLLOW_REQ)))
                .collect(Collectors.toList()).get(0);
        notificationRepository.deleteNotification(request.getId());
    }
}