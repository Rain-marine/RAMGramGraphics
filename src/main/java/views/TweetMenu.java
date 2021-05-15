package views;

import controllers.MessageController;
import controllers.ProfileAccessController;
import controllers.TweetController;
import controllers.UserController;
import models.Tweet;
import views.profiles.FollowingProfile;

import java.util.HashMap;
import java.util.List;

public class TweetMenu extends Menu {
    private final TweetController tweetController;
    private final HashMap<String, Integer> commands;
    private final UserController userController;
    private List<Tweet> tweetsList;
    private int previousMenu;
    private final List<Tweet> parentList;
    private final MessageController messageController;


    public TweetMenu(List<Tweet> listOfTweets, int previousMenu) {
        this.userController = new UserController();
        this.tweetController = new TweetController();
        this.parentList = listOfTweets;
        this.tweetsList = listOfTweets;
        this.previousMenu = previousMenu;
        this.messageController = new MessageController();
        commands = new HashMap<>() {
            {
                put("back", 0);
                put("next", 1);
                put("previous", 2);
                put("save", 3);
                put("retweet", 4);
                put("forward", 5);
                put("block", 6);
                put("mute", 7);
                put("spam", 8);
                put("profile", 9);
                put("add comment", 10);
                put("comments", 11);
                put("like" , 12);
            }
        };

    }

    public void setPreviousMenu(int previousMenu) {
        this.previousMenu = previousMenu;
    }

    public void setTweetsList(List<Tweet> tweetsList) {
        this.tweetsList = tweetsList;
    }

    @Override
    public void run() {
        if (tweetsList.size() == 0) {
            System.out.println("there are no tweet to show!press enter to go back to main menu");
            scanner.nextLine();
            new MainMenu().run();

        }
        Tweet currentTweet = tweetsList.get(0);
        showCommands();
        Outer:
        while (true) {
            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + currentTweet.getUser().getUsername() + ConsoleColors.RESET +
                    " wrote in " + ConsoleColors.BLUE_BOLD_BRIGHT + currentTweet.getTweetDateTime().toString() + "\n"
                    + ConsoleColors.PURPLE_BACKGROUND + ConsoleColors.BLACK_BOLD + currentTweet.getText() + "\n"
                    + ConsoleColors.RESET + ConsoleColors.YELLOW_BOLD_BRIGHT + currentTweet.getUsersWhoLiked().size()
                    + " people liked");
            Show:
            while (true) {
                String input = scanner.nextLine();
                if (commands.containsKey(input)) {
                    switch (commands.get(input)) {
                        case 0:
                            if (currentTweet.getParentTweet() == null) {
                                break Outer;


                            } else {
                                currentTweet = currentTweet.getParentTweet();
                                //ToDo: fix the getAllTweets part
                                tweetsList = currentTweet.getParentTweet() == null ? parentList : currentTweet.getParentTweet().getComments();
                                break Show;
                            }
                        case 1:
                            if (tweetsList.indexOf(currentTweet) == tweetsList.size() - 1) {
                                System.out.println("There is no more tweets to show");
                                continue;
                            } else {
                                currentTweet = tweetsList.get(tweetsList.indexOf(currentTweet) + 1);
                                break Show;
                            }
                        case 2:
                            if (tweetsList.indexOf(currentTweet) == 0) {
                                System.out.println("There is no previous tweet to show");
                                continue;
                            } else {
                                currentTweet = tweetsList.get(tweetsList.indexOf(currentTweet) - 1);
                                break Show;
                            }
                        case 3:
                            //ToDo try catch tweet already saved
                            tweetController.saveTweet(currentTweet.getId());
                            System.out.println("Tweet Saved!");
                            continue;
                        case 4:
                            //ToDo
                            tweetController.retweet(currentTweet);
                            System.out.println("retweet successful");
                            break;
                        case 5:
                            //ToDo
                            forwardTweet(currentTweet);
                            break;
                        case 6:
                            //ToDo try catch if user is already blocked
                            userController.blockUser(currentTweet.getUser());
                            System.out.println("User blocked! list will refresh when you reEnter page later");
                            continue;
                        case 7:
                            userController.muteUser(currentTweet.getUser());
                            System.out.println("User muted! list will refresh when you reEnter page later");
                            continue;
                        case 8:
                            boolean isDeleted = tweetController.reportSpam(currentTweet);
                            System.out.println("Tweet was reported and won't be shown to you again! list will refresh when you reEnter page later");
                            if(isDeleted){
                                System.out.println("you need to refresh the list. press enter to go back");
                                scanner.nextLine();
                                getMenu(previousMenu).run();
                                break;
                            }
                            else {
                                continue;
                            }
                        case 9:
                            if (previousMenu == 0){
                                new FollowingProfile(currentTweet.getUser(),getMenu(previousMenu)).run();
                            }
                            else {
                                ProfileAccessController profileAccessController = new ProfileAccessController(
                                        getMenu(previousMenu), currentTweet.getUser());
                                Menu profile = profileAccessController.checkFollowing();
                                profile.run();
                            }
                            break;
                        case 10:
                            addNewComment(currentTweet);
                            break;
                        case 11:
                            if (currentTweet.getComments().size() == 0) {
                                System.out.println("No Comment to show");
                            } else {
                                tweetsList = currentTweet.getComments();
                                currentTweet = tweetsList.get(0);
                                break Show;
                            }
                            break;
                        case 12:
                            if (!tweetController.like(currentTweet))
                                System.out.println("you've already liked this tweet!");
                            else
                                System.out.println("liked successfully");

                    }
                } else {
                    System.out.println("Invalid input. Try again.");
                }

            }

        }
        getMenu(previousMenu).run();
    }


    private void addNewComment(Tweet parentTweet) {
        System.out.println("write your comment and press enter");
        String comment = scanner.nextLine();
        tweetController.addComment(comment,null, parentTweet);
    }

    private void forwardTweet(Tweet tweet) {
        while(true) {
            System.out.println("type username of receiver and press enter or *back to return!");
            String receiver = scanner.nextLine();
            if(receiver.equals("*back"))
                return;
            if(messageController.canSendMessageToUser(receiver)) {
                messageController.forwardTweet(tweet, tweet.getUser(), receiver);
                System.out.println("The tweet forwarded!");
                return;
            }
            System.out.println("The username is invalid!");
        }
    }

    private void showCommands() {
        System.out.println("type the command code. here's the list of command and what they do"
                + "\n" + "back -> back to previous step"
                + "\n" + "next -> next tweet or comment"
                + "\n" + "previous -> previous tweet or comment"
                + "\n" + "save -> add tweet to your favorite"
                + "\n" + "retweet -> retweet the tweet you are seeing"
                + "\n" + "forward -> send tweet for another user"
                + "\n" + "block -> block writer of the tweet"
                + "\n" + "mute-> mute writer of the tweet"
                + "\n" + "spam -> report tweet spam"
                + "\n" + "profile -> go to profile writer of the tweet"
                + "\n" + "add comment -> add comment"
                + "\n" + "comments -> show comments"
                + "\n" + "like -> like the tweet");

    }

    @Override
    public Menu getMenu(int option) {
        if (option == 0)
            return new TimelineMenu();
        if (option == 1)
            return new ExplorerMenu();
        return new MainMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        return false;
    }

}
