package controllers;

import models.LoggedUser;
import models.Tweet;
import models.User;
import repository.TweetRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TweetController {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final FactionsController factionsController;

    public TweetController() {
        userRepository = new UserRepository();
        tweetRepository = new TweetRepository();
        factionsController = new FactionsController();
    }


    public void addTweet(String text, byte[] image){
        Tweet tweet = new Tweet(LoggedUser.getLoggedUser(),text, image);
        tweetRepository.insert(tweet);
    }

    public ArrayList<Long> getAllTweets(long userId) {
        User user = userRepository.getById(userId);
        List<Tweet> userAllTweets = tweetRepository.getAllTweets(user.getId());
        userAllTweets.addAll(user.getRetweetTweets());
        List<Tweet> finalTweets =  userAllTweets.stream().sorted(Comparator.comparing(Tweet::getTweetDateTime)).
                collect(Collectors.toList());
        ArrayList<Long> finalTweetsIDs = new ArrayList<>();
        for (Tweet finalTweet : finalTweets) {
            finalTweetsIDs.add(finalTweet.getId());
        }
        return finalTweetsIDs;
    }

    public ArrayList<Long> getTopTweets() {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<Tweet> topTweets = tweetRepository.getTopTweets(LoggedUser.getLoggedUser().getId());
        ArrayList<Long> topTweetsIDs = new ArrayList<>();
        for (Tweet topTweet : topTweets) {
            User topTweetUser = userRepository.getById(topTweet.getUser().getId());
            if (topTweetUser.getBlackList().stream().noneMatch(it -> it.getId() == LoggedUser.getLoggedUser().getId())) {
                if(loggedUser.getMutedUsers().stream().noneMatch(it -> it.getId() == topTweetUser.getId()))
                    topTweetsIDs.add(topTweet.getId());
            }
        }
        return topTweetsIDs;



    }

    public ArrayList<Long> getFollowingTweets() {
        List<Tweet> followingTweets = new ArrayList<>();
        User currentUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<User> following = factionsController.getActiveFollowings();
        List<User> muted = currentUser.getMutedUsers();

        for (User user : following) {
            if (muted.stream().noneMatch(it -> it.getId() == user.getId())) {
                followingTweets.addAll(getAllTweetsModel(user));
            }
        }
        List<Tweet> finalTweetList = followingTweets.stream().sorted(Comparator.comparing(Tweet::getTweetDateTime).reversed()).
                 collect(Collectors.toList());
        ArrayList<Long> finalTweetsIDs = new ArrayList<>();
        for (Tweet tweet : finalTweetList) {
            finalTweetsIDs.add(tweet.getId());
        }
        return finalTweetsIDs;
    }

    private List<Tweet> getAllTweetsModel(User rawUser) {
        User user = userRepository.getByUsername(rawUser.getUsername());
        List<Tweet> userAllTweets = tweetRepository.getAllTweets(user.getId());
        userAllTweets.addAll(user.getRetweetTweets());
        return userAllTweets.stream().sorted(Comparator.comparing(Tweet::getTweetDateTime)).
                collect(Collectors.toList());
    }

    public void saveTweet(long tweetId) {
        userRepository.addFavoriteTweet(LoggedUser.getLoggedUser().getId(), tweetId);

    }

    public void retweet(long currentTweetId) {
        userRepository.addRetweet(currentTweetId,LoggedUser.getLoggedUser().getId());
    }

    public boolean reportSpam(long currentTweetId) {
        Tweet reportedTweet = tweetRepository.getById(currentTweetId);
        if (reportedTweet.getReportCounter() >= 2 ){
            tweetRepository.delete(reportedTweet.getId());
            return true;
        }
        else {
            tweetRepository.increaseReportCount(currentTweetId);
            userRepository.addReportedTweet(currentTweetId, LoggedUser.getLoggedUser().getId());
        }
        return false;
    }

    public void addComment(String comment,byte[] image , long rawParentTweetId) {
        Tweet parentTweet = tweetRepository.getById(rawParentTweetId);
        Tweet commentTweet = new Tweet(userRepository.getById(LoggedUser.getLoggedUser().getId()),comment, image);
        tweetRepository.addComment(parentTweet,commentTweet);
    }

    public boolean isLiked (long tweetId){
        Tweet completeTweet = tweetRepository.getById(tweetId);
        for (User user : completeTweet.getUsersWhoLiked()) {
            if (user.getUsername().equals(LoggedUser.getLoggedUser().getUsername()))
                return true;
        }
        return false;

    }

    public boolean like(long tweetId) {
        Tweet completeTweet = tweetRepository.getById(tweetId);
        for (User user : completeTweet.getUsersWhoLiked()) {
            if (user.getUsername().equals(LoggedUser.getLoggedUser().getUsername()))
                return false;
        }
        tweetRepository.like(LoggedUser.getLoggedUser().getId(), tweetId);
        return true;
    }

    public boolean isSelfTweet(long tweetId) {
        Tweet tweet = tweetRepository.getById(tweetId);
        return tweet.getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername());
    }

    public long getWriterId(long tweetId) {
        return tweetRepository.getById(tweetId).getUser().getId();
    }

    public String getTweetText(long tweetId) {
        return tweetRepository.getById(tweetId).getText();
    }

    public String getWriterUsername(long tweetId) {
        return tweetRepository.getById(tweetId).getUser().getUsername();
    }

    public String getTweetDate(long tweetId) {
        return tweetRepository.getById(tweetId).getTweetDateTime().toString();
    }

    public byte[] getTweetImage(long tweetId) {
        return tweetRepository.getById(tweetId).getImage();
    }

    public ArrayList<Long> getTweetComments(long tweetId) {
        List<Tweet> comments = tweetRepository.getById(tweetId).getComments();
        ArrayList<Long> commentId = new ArrayList<>();
        for (Tweet comment : comments) {
            commentId.add(comment.getId());
        }
        return commentId;
    }
}
