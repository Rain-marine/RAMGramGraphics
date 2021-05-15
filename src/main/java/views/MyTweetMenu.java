package views;

import controllers.MessageController;
import controllers.ProfileAccessController;
import controllers.TweetController;
import controllers.UserController;
import models.LoggedUser;
import models.Tweet;

import java.util.HashMap;
import java.util.List;

public class MyTweetMenu extends Menu {
    private final TweetController tweetController;
    private final HashMap<String, Integer> commands;
    private final MessageController messageController;
    private final UserController userController;
    private List<Tweet> tweetsList;

    public MyTweetMenu() {
        this.userController = new UserController();
        this.tweetController = new TweetController();
        this.tweetsList = tweetController.getAllTweets(LoggedUser.getLoggedUser());
        commands = new HashMap<>() {
            {
                put("back", 0);
                put("next", 1);
                put("previous", 2);
                put("save", 3);
                put("forward", 5);
                put("profile", 9);
                put("add comment", 10);
                put("comments", 11);
            }
        };

        messageController = new MessageController();
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
                                tweetsList = currentTweet.getParentTweet() == null ? tweetController.getAllTweets(LoggedUser.getLoggedUser()) : currentTweet.getParentTweet().getComments();
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
                        case 5:
                            forwardTweet(currentTweet);
                            break;
                        case 9:
                            ProfileAccessController profileAccessController = new ProfileAccessController(
                                    getMenu(3), currentTweet.getUser());
                            Menu profile = profileAccessController.checkFollowing();
                            profile.run();
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

                    }
                } else {
                    System.out.println("Invalid input. Try again.");
                }

            }
        }
        getMenu(3).run();
    }


    private void addNewComment(Tweet parentTweet) {
        System.out.println("write your comment and press enter");
        String comment = scanner.nextLine();
        tweetController.addComment(comment,null, parentTweet);
    }

    private void forwardTweet(Tweet tweet) {
        //todo : forward tweet is done!. check?
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
                + "\n" + "forward -> send tweet for another user"
                + "\n" + "profile -> go to profile writer of the tweet"
                + "\n" + "add comment -> add comment"
                + "\n" + "comments -> show comments");

    }

    @Override
    public Menu getMenu(int option) {
        if (option == 3)
            return new PersonalPageMenu();
        return new MainMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        return false;
    }
}
