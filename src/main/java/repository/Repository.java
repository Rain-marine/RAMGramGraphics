package repository;

public interface Repository {
    UserRepository USER_REPOSITORY = new UserRepository();
    FactionRepository FACTION_REPOSITORY = new FactionRepository();
    NotificationRepository NOTIFICATION_REPOSITORY = new NotificationRepository();
    TweetRepository TWEET_REPOSITORY = new TweetRepository();
    ChatRepository CHAT_REPOSITORY = new ChatRepository();
    MessageRepository MESSAGE_REPOSITORY = new MessageRepository();

}
