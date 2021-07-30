package gui.controllers;

import controllers.ProfileAccessController;
import gui.controllers.popups.AlertBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.LoggedUser;
import models.Tweet;
import models.User;

import javax.naming.SizeLimitExceededException;

public class TweetCard implements Controllers {

    private Tweet tweet;
    private VBox vBox;
    private Label tweetText;
    private ImageView profilePhoto;
    private Label dateTime;

    private Button writerName;
    private Button save;
    private Button forward;
    private Button comments;

    private TextField commentText = new TextField();
    private Button addComment;
    private VBox addCommentLayout = new VBox(5);
    private Button commentImage;
    private byte[] commentImageArray;

    private Button retweet;
    private Button block;
    private Button mute;
    private Button report;
    private Button like;

    private User writer;
    private HBox header;
    private HBox buttons = new HBox(10);
    private HBox generalButtons = new HBox(10);
    private ImageView tweetPhoto;

    public enum MODE {OWNER, TIMELINE, EXPLORER, PROFILE}


    public TweetCard(Tweet tweet, MODE mode) {
        this.tweet = tweet;
        if (tweet.getUser().getUsername().equals(LoggedUser.getLoggedUser().getUsername())){
            mode = MODE.OWNER;
        }
        MODE finalMode = mode;
        vBox = new VBox(10);
        tweetText = new Label(tweet.getText());
        tweetText.setWrapText(true);
        writer = tweet.getUser();

        writerName = new Button(writer.getUsername());
        writerName.setOnAction(event -> {
            if (finalMode != MODE.PROFILE) {
                ProfileAccessController profileAccessController = new ProfileAccessController(finalMode == MODE.EXPLORER ? 1 : (finalMode == MODE.TIMELINE ? 2 : 3), writer, 0);
                Toolbar.getInstance().changeScene(profileAccessController.checkAccessibility(), event);
            }
        });
        writerName.setStyle("-fx-background-color: #dea0ff");
        writerName.setPrefHeight(50);

        dateTime = new Label(tweet.getTweetDateTime().toString());
        dateTime.setTextFill(Color.DARKVIOLET);

        profilePhoto = new ImageView();
        profilePhoto.setFitHeight(50);
        profilePhoto.setFitWidth(50);
        byte[] byteArray = writer.getProfilePhoto();
        Rectangle clip = new Rectangle(
                profilePhoto.getFitWidth(), profilePhoto.getFitHeight()
        );
        clip.setArcWidth(1000);
        clip.setArcHeight(1000);
        profilePhoto.setClip(clip);
        profilePhoto.setImage(ImageController.byteArrayToImage(byteArray));
        header = new HBox(10);
        header.getChildren().addAll(profilePhoto, writerName , dateTime);
        tweetPhoto = new ImageView();
        if (tweet.getImage() != null) {
            tweetPhoto.setImage(ImageController.byteArrayToImage(tweet.getImage()));
            tweetPhoto.setPreserveRatio(true);
            tweetPhoto.setFitWidth(350);
        }

        save = new Button("save");
        save.setStyle("-fx-background-color: #dea0ff");
        save.setOnAction(event -> {
            TWEET_CONTROLLER.saveTweet(tweet.getId());
            AlertBox.display("done!", "tweet saved");
        });

        forward = new Button("forward");
        forward.setStyle("-fx-background-color: #dea0ff");
        forward.setOnAction(event -> {
        });
        //todo

        comments = new Button("comments");
        comments.setStyle("-fx-background-color: #dea0ff");
        comments.setOnAction(event -> {
            if (tweet.getComments().size() == 0) {
                AlertBox.display("empty", "no comments to show");
            } else {
                TweetShowerGuiController.setListOfTweets(tweet.getComments());
                TweetShowerGuiController.setPreviousMenu( finalMode == MODE.EXPLORER ? 1 : (finalMode ==MODE.TIMELINE ? 2 : (finalMode == MODE.OWNER ? 6 : 5)));
                Toolbar.getInstance().changeScene("FXMLs/TweetShower.fxml" ,event);
            }
        });

        commentImage = new Button("add Image");
        commentImage.setOnAction(event -> {
            try {
                commentImageArray = ImageController.pickImage();
            } catch (SizeLimitExceededException e) {
                AlertBox.display("size limit error","Image size is too large. \nImage size should be less than 2MB");
            }
        });
        addComment = new Button("add comment");
        addComment.setStyle("-fx-background-color: #dea0ff");
        addComment.setOnAction(event -> {
            String commentTextString = commentText.getText();
            if(!commentTextString.equals("")){
                TWEET_CONTROLLER.addComment(commentTextString , commentImageArray == null ? null :commentImageArray , tweet);
            }
        });
        HBox row = new HBox(5);
        row.getChildren().addAll(commentImage, addComment);
        addCommentLayout.getChildren().addAll(commentText, row);
        generalButtons.getChildren().addAll(save , forward, comments);

        if (finalMode != MODE.OWNER) {

            retweet = new Button("retweet");
            retweet.setStyle("-fx-background-color: #dea0ff");
            retweet.setOnAction(event -> {
                TWEET_CONTROLLER.retweet(tweet);
                AlertBox.display("done!", "retweeted!");
            });

            block = new Button("block");
            block.setStyle("-fx-background-color: #dea0ff");
            block.setOnAction(event -> {
                USER_CONTROLLER.blockUser(tweet.getUser());
                AlertBox.display("done!", "user blocked");
            });

            mute = new Button("mute");
            mute.setStyle("-fx-background-color: #dea0ff");
            mute.setOnAction(event -> {
                USER_CONTROLLER.muteUser(tweet.getUser());
                AlertBox.display("done!", "user muted!");
            });

            report = new Button("report");
            report.setStyle("-fx-background-color: #dea0ff");
            report.setOnAction(event -> {
                boolean isDeleted = TWEET_CONTROLLER.reportSpam(tweet);
                if (isDeleted) {
                    AlertBox.display("refresh", "you need to refresh the page");
                } else {
                    AlertBox.display("done!", "tweet reported!");
                }
            });

            boolean isLiked = TWEET_CONTROLLER.isLiked(tweet);
            like = new Button(isLiked ? "liked" : "like");
            like.setStyle("-fx-background-color: #dea0ff");
            like.setOnAction(event -> {
                if (!isLiked) {
                    TWEET_CONTROLLER.like(tweet);
                    like.setText("liked");
                }
            });

            buttons.getChildren().addAll(like, report, retweet, block, mute);
            vBox.getChildren().addAll(header, tweetText, tweetPhoto, generalButtons, buttons ,  addCommentLayout);
        } else {
            vBox.getChildren().addAll(header, tweetText, tweetPhoto, generalButtons ,  addCommentLayout);
        }


    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }
}
