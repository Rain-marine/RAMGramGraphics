package views;

import controllers.NotificationController;
import models.Notification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NotificationMenu extends Menu {
    private final NotificationController notificationController;

    public NotificationMenu() {
        notificationController = new NotificationController();
        options = Arrays.asList("following requests", "your following requests", "system notification", "back");
    }

    @Override
    public void run() {

        Input:
        while (true) {
            System.out.println("**Notification Menu**");
            showOptions();
            System.out.println("You are in notification menu! enter your request!");
            String input = scanner.nextLine();
            if (!options.contains(input)) {
                System.out.println("Invalid input!! Enter again");
                continue;
            }
            switch (input) {
                case "following requests":
                    handleFollowingRequestNotification();
                    break;
                case "your following requests":
                    handleYourFollowingRequestNotification();
                    break;
                case "system notification":
                    handleSystemNotification();
                    break;
                case "back":
                    break Input;
            }
        }
        new PersonalPageMenu().run();
    }

    private void handleSystemNotification() {
        List<Notification> notifications = notificationController.getSystemNotification();
        if (notifications.size() == 0) {
            System.out.println("You have no system notification!");
        } else {
            for (Notification notification : notifications) {
                switch (notification.getType()) {
                    case UNFOLLOW -> System.out.println(notification.getSender().getUsername() + " unfollowed you!");
                    case START_FOLLOW -> System.out.println(notification.getSender().getUsername() + " started follow you!");
                    case FOLLOW_REQ_REJECT -> System.out.println(notification.getSender().getUsername() + " rejected your follow request!");
                }
                notificationController.deleteNotification(notification);
            }
        }
        System.out.println("press any key to back to notification menu!");
        scanner.nextLine();
    }

    private void handleYourFollowingRequestNotification() {
        List<Notification> notifications = notificationController.getYourFollowingRequestNotification();
        if (notifications.size() == 0) {
            System.out.println("You have no request! consider maybe people reject your request without notification!");
        } else {
            for (Notification notification : notifications) {
                System.out.println(notification.getReceiver().getUsername());
            }
            System.out.println("if someone accept or reject your request, then it will not be displayed here!");
        }
        System.out.println("press any key to continue to notification menu!");
        scanner.nextLine();
    }

    private void handleFollowingRequestNotification() {
        List<Notification> followingRequestNotification = notificationController.getFollowingRequestsNotifications();
        if (followingRequestNotification.size() == 0) {
            System.out.println("YOU HAVE NO FOLLOW REQUEST!");
            return;
        }

        HashMap<String, Notification> userToNotification = extractUserFromNotification(followingRequestNotification);
        while (true) {
            for (Notification notification : followingRequestNotification)
                System.out.println(notification.getSender().getUsername());

            System.out.println("enter the username to handle the request or \"*back\" to back");
            String input = scanner.nextLine();
            if (input.equals("*back"))
                return;
            if (!userToNotification.containsKey(input)) {
                System.out.println("username is not correct!");
                continue;
            }
            System.out.println("what do you wanna do?(accept, reject with notification, reject without notification)");
            handleRequest:
            while (true) {
                String order = scanner.nextLine();
                switch (order) {
                    case "accept":
                        notificationController.acceptFollowRequest(userToNotification.get(input));
                        System.out.println("New follower added!");
                        break;
                    case "reject with notification":
                        notificationController.rejectFollowRequestWithNotification(userToNotification.get(input));
                        System.out.println("request rejected and " + input + " will be notified");
                        break;
                    case "reject without notification":
                        notificationController.rejectFollowRequestWithoutNotification(userToNotification.get(input));
                        System.out.println("request rejected and " + input + " will not be notified");
                        break;
                    case "*back":
                        break handleRequest;
                    default:
                        System.out.println("Invalid request! enter again");
                        continue;
                }
                followingRequestNotification.remove(userToNotification.get(input));
                userToNotification.remove(input);
                if (userToNotification.isEmpty()) {
                    System.out.println("Your following request is empty, you get back to notification menu!\npress any key to continue");
                    scanner.nextLine();
                    return;
                }
                break;
            }
        }
    }

    private HashMap<String, Notification> extractUserFromNotification(List<Notification> notifications) {
        HashMap<String, Notification> userToNotification = new HashMap<>();
        for (Notification notification : notifications) {
            userToNotification.put(notification.getSender().getUsername(), notification);
        }
        return userToNotification;
    }


    private void showOptions() {
        System.out.println("following requests -> see people request to follow you\n" +
                "your following requests -> see your request to follow people\n" +
                "system notification -> see follow and unfollow notifications\n" +
                "back -> back to personal page menu");
    }

    @Override
    public Menu getMenu(int option) {
        return null;
    }
}
