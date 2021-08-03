package gui.controllers.tweets;

import controllers.ProfileAccessController;
import gui.controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
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
import util.ConfigLoader;

import javax.naming.SizeLimitExceededException;

public class TweetCard implements Controllers {

    private long tweetId;
    private long writeId;
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

    private HBox header;
    private HBox buttons = new HBox(10);
    private HBox generalButtons = new HBox(10);
    private ImageView tweetPhoto;

    public enum MODE {OWNER, TIMELINE, EXPLORER, PROFILE}


    public TweetCard(long tweetId, MODE mode) {
        this.tweetId = tweetId;
        if (TWEET_CONTROLLER.isSelfTweet(tweetId)){
            mode = MODE.OWNER;
        }
        MODE finalMode = mode;
        writeId = TWEET_CONTROLLER.getWriterId(tweetId);
        vBox = new VBox(10);
        tweetText = new Label(TWEET_CONTROLLER.getTweetText(tweetId));
        tweetText.setWrapText(true);

        writerName = new Button(TWEET_CONTROLLER.getWriterUsername(tweetId));
        writerName.setOnAction(event -> {
            if (finalMode != MODE.PROFILE) {
                ProfileAccessController profileAccessController = new ProfileAccessController(finalMode == MODE.EXPLORER ? 1 : (finalMode == MODE.TIMELINE ? 2 : 3), writeId, 0);
                SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(), event);
            }
        });
        writerName.setStyle("-fx-background-color: #dea0ff");
        writerName.setPrefHeight(50);

        dateTime = new Label(TWEET_CONTROLLER.getTweetDate(tweetId));
        dateTime.setTextFill(Color.DARKVIOLET);

        profilePhoto = new ImageView();
        profilePhoto.setFitHeight(50);
        profilePhoto.setFitWidth(50);
        byte[] byteArray = USER_CONTROLLER.getProfilePhoto(writeId);
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
        if (TWEET_CONTROLLER.getTweetImage(tweetId) != null) {
            tweetPhoto.setImage(ImageController.byteArrayToImage(TWEET_CONTROLLER.getTweetImage(tweetId)));
            tweetPhoto.setPreserveRatio(true);
            tweetPhoto.setFitWidth(350);
        }

        save = new Button("save");
        save.setStyle("-fx-background-color: #dea0ff");
        save.setOnAction(event -> {
            TWEET_CONTROLLER.saveTweet(tweetId);
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
            if (TWEET_CONTROLLER.getTweetComments(tweetId).size() == 0) {
                AlertBox.display("empty", "no comments to show");
            } else {
                TweetShowerGuiController.setListOfTweets(TWEET_CONTROLLER.getTweetComments(tweetId));
                TweetShowerGuiController.setPreviousMenu( finalMode == MODE.EXPLORER ? 1 : (finalMode ==MODE.TIMELINE ? 2 : (finalMode == MODE.OWNER ? 6 : 5)));
                SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("tweetShower"),event);
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
                TWEET_CONTROLLER.addComment(commentTextString , commentImageArray == null ? null :commentImageArray , tweetId);
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
                TWEET_CONTROLLER.retweet(tweetId);
                AlertBox.display("done!", "retweeted!");
            });

            block = new Button("block");
            block.setStyle("-fx-background-color: #dea0ff");
            block.setOnAction(event -> {
                USER_CONTROLLER.blockUser(writeId);
                AlertBox.display("done!", "user blocked");
            });

            mute = new Button("mute");
            mute.setStyle("-fx-background-color: #dea0ff");
            mute.setOnAction(event -> {
                USER_CONTROLLER.muteUser(writeId);
                AlertBox.display("done!", "user muted!");
            });

            report = new Button("report");
            report.setStyle("-fx-background-color: #dea0ff");
            report.setOnAction(event -> {
                boolean isDeleted = TWEET_CONTROLLER.reportSpam(tweetId);
                if (isDeleted) {
                    AlertBox.display("refresh", "you need to refresh the page");
                } else {
                    AlertBox.display("done!", "tweet reported!");
                }
            });

            boolean isLiked = TWEET_CONTROLLER.isLiked(tweetId);
            like = new Button(isLiked ? "liked" : "like");
            like.setStyle("-fx-background-color: #dea0ff");
            like.setOnAction(event -> {
                if (!isLiked) {
                    TWEET_CONTROLLER.like(tweetId);
                    like.setText("liked");
                }
            });

            buttons.getChildren().addAll(like, report, retweet, block, mute);
            vBox.getChildren().addAll(header, tweetText, tweetPhoto, generalButtons, buttons ,  addCommentLayout);
        } else {
            vBox.getChildren().addAll(header, tweetText, tweetPhoto, generalButtons ,  addCommentLayout);
        }


    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public VBox getVBox() {
        return vBox;
    }

    public void setVBox(VBox vBox) {
        this.vBox = vBox;
    }
}
