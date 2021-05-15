package controllers;

import models.LoggedUser;
import models.Tweet;
import models.User;
import repository.FactionRepository;
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

    public List<Tweet> getAllTweets(User user2) {
        String username = user2.getUsername();
        User user = userRepository.getByUsername(username);
        List<Tweet> userAllTweets = tweetRepository.getAllTweets(user.getId());
        userAllTweets.addAll(user.getRetweetTweets());
        return userAllTweets.stream().sorted(Comparator.comparing(Tweet::getTweetDateTime)).
                collect(Collectors.toList());
    }

    public List<Tweet> getTopTweets() {
        User loggedUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<Tweet> topTweets = tweetRepository.getTopTweets(LoggedUser.getLoggedUser().getId());
        List<Tweet> topTweets2 = new ArrayList<>();
        for (Tweet topTweet : topTweets) {
            User topTweetUser = userRepository.getById(topTweet.getUser().getId());
            if (topTweetUser.getBlackList().stream().noneMatch(it -> it.getId() == LoggedUser.getLoggedUser().getId())) {
                if(loggedUser.getMutedUsers().stream().noneMatch(it -> it.getId() == topTweetUser.getId()))
                    topTweets2.add(topTweet);
            }
        }
        return topTweets2;



    }

    public List<Tweet> getFollowingTweets() {
        List<Tweet> followingTweets = new ArrayList<>();
        User currentUser = userRepository.getById(LoggedUser.getLoggedUser().getId());
        List<User> following = factionsController.getActiveFollowings();
        List<User> muted = currentUser.getMutedUsers();

        for (User user : following) {
            if (muted.stream().noneMatch(it -> it.getId() == user.getId())) {
                followingTweets.addAll(getAllTweets(user));
            }
        }
        return followingTweets.stream().sorted(Comparator.comparing(Tweet::getTweetDateTime).reversed()).
                 collect(Collectors.toList());
    }

    public void saveTweet(long tweetId) {
        userRepository.addFavoriteTweet(LoggedUser.getLoggedUser().getId(), tweetId);

    }

    public void retweet(Tweet currentTweet) {
        userRepository.addRetweet(currentTweet.getId(),LoggedUser.getLoggedUser().getId());
    }

    public boolean reportSpam(Tweet currentTweet) {
        Tweet reportedTweet = tweetRepository.getById(currentTweet.getId());
        if (reportedTweet.getReportCounter() >= 2 ){
            tweetRepository.delete(reportedTweet.getId());
            return true;
        }
        else {
            tweetRepository.increaseReportCount(currentTweet.getId());
            userRepository.addReportedTweet(currentTweet.getId(), LoggedUser.getLoggedUser().getId());
        }
        return false;
    }

    public void addComment(String comment,byte[] image , Tweet rawParentTweet) {
        Tweet parentTweet = tweetRepository.getById(rawParentTweet.getId());
        Tweet commentTweet = new Tweet(userRepository.getById(LoggedUser.getLoggedUser().getId()),comment, image);
        tweetRepository.addComment(parentTweet,commentTweet);
    }

    public boolean like(Tweet tweet) {
        Tweet completeTweet = tweetRepository.getById(tweet.getId());
        for (User user : completeTweet.getUsersWhoLiked()) {
            if (user.getUsername().equals(LoggedUser.getLoggedUser().getUsername()))
                return false;
        }
        tweetRepository.like(LoggedUser.getLoggedUser().getId(), tweet.getId());
        return true;
    }
}
