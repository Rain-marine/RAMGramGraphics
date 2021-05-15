package models;

public enum NotificationType {
    FOLLOW_REQ_REJECT("Your follow request rejected!"),
    START_FOLLOW("New Follower!"),
    UNFOLLOW("Follower gone!"),
    FOLLOW_REQ("New request to follow you");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
