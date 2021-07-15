package gui.controllers;

import controllers.NotificationController;
import controllers.SettingController;
import controllers.TweetController;
import controllers.UserController;

public interface Controllers {
    NotificationController NOTIFICATION_CONTROLLER = new NotificationController();
    SettingController SETTING_CONTROLLER = new SettingController();
    UserController USER_CONTROLLER = new UserController();
    TweetController TWEET_CONTROLLER = new TweetController();
}
