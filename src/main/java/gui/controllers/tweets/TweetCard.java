package gui.controllers.tweets;

import controllers.ProfileAccessController;
import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import util.ConfigLoader;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;

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
    private Label likedNumber;

    public enum MODE {OWNER, TIMELINE, EXPLORER, PROFILE}


    public TweetCard(long tweetId, MODE mode) {
        this.tweetId = tweetId;
        if (TWEET_CONTROLLER.isSelfTweet(tweetId)){
            mode = MODE.OWNER;
        }
        MODE finalMode = mode;
        writeId = TWEET_CONTROLLER.getWriterId(tweetId);
        vBox = new VBox(5);
        vBox.setPadding(new Insets(0,0,0,0));
        vBox.setPrefWidth(430);
        vBox.setStyle("-fx-background-color: #000000");
        tweetText = new Label(TWEET_CONTROLLER.getTweetText(tweetId));
        tweetText.setWrapText(true);
        tweetText.setTextFill(Color.SNOW);
        tweetText.setFont(Font.font(15));

        writerName = new Button(TWEET_CONTROLLER.getWriterUsername(tweetId));
        writerName.setOnAction(event -> {
            if (finalMode != MODE.PROFILE) {
                ProfileAccessController profileAccessController = new ProfileAccessController(finalMode == MODE.EXPLORER ? 1 : (finalMode == MODE.TIMELINE ? 2 : 3), writeId, 0);
                SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(), event);
            }
        });
        writerName.setStyle("-fx-background-color: #000000");
        writerName.setPrefHeight(50);
        writerName.setTextFill(Color.MEDIUMORCHID);
        writerName.setFont(Font.font("Arial" , FontWeight.BOLD , 18 ));

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

        tweetPhoto = new ImageView();
        if (TWEET_CONTROLLER.getTweetImage(tweetId) != null) {
            tweetPhoto.setImage(ImageController.byteArrayToImage(TWEET_CONTROLLER.getTweetImage(tweetId)));
            tweetPhoto.setPreserveRatio(true);
            tweetPhoto.setFitWidth(350);
        }

        save = new Button("save");
        save.setStyle("-fx-background-color: #690081");
        save.setTextFill(Color.LEMONCHIFFON);
        save.setOnAction(event -> {
            TWEET_CONTROLLER.saveTweet(tweetId);
            AlertBox.display("done!", "tweet saved");
        });

        forward = new Button("forward");
        forward.setStyle("-fx-background-color: #690081");
        forward.setTextFill(Color.LEMONCHIFFON);
        forward.setOnAction(event -> {
            ForwardTweet.display(tweetId);
        });


        comments = new Button("comments");
        comments.setStyle("-fx-background-color: #690081");
        comments.setTextFill(Color.LEMONCHIFFON);
        comments.setOnAction(event -> {
            if (TWEET_CONTROLLER.getTweetComments(tweetId).size() == 0) {
                AlertBox.display("empty", "no comments to show");
            } else {
                TweetShowerGuiController.setListOfTweets(TWEET_CONTROLLER.getTweetComments(tweetId));
                TweetShowerGuiController.setPreviousMenu( finalMode == MODE.EXPLORER ? 1 : (finalMode ==MODE.TIMELINE ? 2 : (finalMode == MODE.OWNER ? 6 : 5)));
                SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("tweetShower"),event);
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

        commentText.setPromptText("write comment");
        commentText.setStyle("-fx-background-color: #36214d;" + "-fx-text-fill: #ffd29e;");
        commentText.setMaxWidth(300);
        addComment = new Button("add comment");
        addComment.setStyle("-fx-background-color: #690081");
        addComment.setTextFill(Color.LEMONCHIFFON);
        addComment.setOnAction(event -> {
            String commentTextString = commentText.getText();
            if(!commentTextString.equals("")){
                TWEET_CONTROLLER.addComment(commentTextString , commentImageArray == null ? null :commentImageArray , tweetId);
            }
        });
        commentImage.setStyle("-fx-background-color: #690081");
        commentImage.setTextFill(Color.LEMONCHIFFON);

        Label separator = new Label("*********************************************");
        separator.setTextFill(Color.VIOLET);
        HBox row = new HBox(5);
        row.getChildren().addAll(commentImage, addComment);
        addCommentLayout.getChildren().addAll(commentText, row);
        generalButtons.getChildren().addAll(save , forward, comments);

        likedNumber = new Label();
        likedNumber.setTextFill(Color.MAGENTA);
        ArrayList<String> peopleLiked = TWEET_CONTROLLER.getLikedList(tweetId);
        likedNumber.setText("liked by " + peopleLiked.size() + " people: "+ String.join(", ", peopleLiked));

        if (finalMode != MODE.OWNER) {

            retweet = new Button("retweet");
            retweet.setStyle("-fx-background-color: #690081");
            retweet.setTextFill(Color.LEMONCHIFFON);
            retweet.setOnAction(event -> {
                TWEET_CONTROLLER.retweet(tweetId);
                AlertBox.display("done!", "retweeted!");
            });

            block = new Button("block");
            block.setStyle("-fx-background-color: #690081");
            block.setTextFill(Color.LEMONCHIFFON);
            block.setOnAction(event -> {
                USER_CONTROLLER.blockUser(writeId);
                AlertBox.display("done!", "user blocked");
            });

            mute = new Button("mute");
            mute.setStyle("-fx-background-color: #690081");
            mute.setTextFill(Color.LEMONCHIFFON);
            mute.setOnAction(event -> {
                USER_CONTROLLER.muteUser(writeId);
                AlertBox.display("done!", "user muted!");
            });

            report = new Button("report");
            report.setStyle("-fx-background-color: #690081");
            report.setTextFill(Color.LEMONCHIFFON);
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
            like.setStyle("-fx-background-color: #690081");
            like.setTextFill(Color.LEMONCHIFFON);
            like.setOnAction(event -> {
                if (!isLiked) {
                    TWEET_CONTROLLER.like(tweetId);
                    like.setText("liked");
                    updateLikedList();
                }
            });
            header.getChildren().addAll(profilePhoto, writerName );
            header.getChildren().add(new VBox(5, dateTime , generalButtons));
            buttons.getChildren().addAll(like, report, retweet, block, mute);
            vBox.getChildren().addAll(header, tweetText, tweetPhoto , likedNumber, buttons ,  addCommentLayout , separator);
        } else {
            vBox.getChildren().addAll(header, tweetText, tweetPhoto , likedNumber ,  addCommentLayout , separator);
        }


    }

    public void updateLikedList(){
        likedNumber.setTextFill(Color.MAGENTA);
        ArrayList<String> peopleLiked = TWEET_CONTROLLER.getLikedList(tweetId);
        likedNumber.setText("liked by " + peopleLiked.size() + " people: "+ String.join(", ", peopleLiked));

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
