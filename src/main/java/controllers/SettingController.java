package controllers;


import models.LoggedUser;
import models.User;
import repository.UserRepository;

import java.util.Date;
import java.util.List;


public class SettingController {
    private final UserRepository userRepository;

    public SettingController() {
        this.userRepository = new UserRepository();
    }

    public void logout() {
        if (!(LoggedUser.getLoggedUser() == null))
            userRepository.setLastSeen(LoggedUser.getLoggedUser().getId(), new Date());
        LoggedUser.setLoggedUser(null);
    }

    public void deleteAccount() {
        userRepository.deleteAccount(LoggedUser.getLoggedUser().getId());
        LoggedUser.setLoggedUser(null);
    }

    public boolean doesPasswordExist(String password) {
        return userRepository.getById(LoggedUser.getLoggedUser().getId()).getPassword().equals(password);
    }

    public boolean isAccountPublic(String username) {
        User user = userRepository.getByUsername(username);
        return user.isPublic();
    }

    public void changeAccountVisibility(boolean newVisibility) {
        userRepository.changeAccountVisibility(LoggedUser.getLoggedUser().getId(), newVisibility);
    }

    public void deActiveAccount() {
        userRepository.deactivateAccount(LoggedUser.getLoggedUser().getId());
        LoggedUser.setLoggedUser(null);
    }

    public String getUserLastSeenStatus(String username) {
        User user = userRepository.getByUsername(username);
        return user.getLastSeenStatus();
    }

    public void changeLastSeenStatus(String newStatus) {
        userRepository.changeLastSeenStatus(LoggedUser.getLoggedUser().getId(), newStatus);
    }

    public void changePassword(String newPassword) {
        userRepository.changePassword(LoggedUser.getLoggedUser().getId(), newPassword);
    }

    public String lastSeenForLoggedUser(User rawUser) {
        User user = userRepository.getById(rawUser.getId());
        long loggedUserId = LoggedUser.getLoggedUser().getId();
        String status = userRepository.getById(user.getId()).getLastSeenStatus();
        if (user.getFollowings().stream().noneMatch(it -> it.getId() == loggedUserId)) {
            return ("last seen recently");
        } else if (status.equals("everybody"))
            return (user.getLastSeen().toString());
        else if (status.equals("following")) {
            List<User> userFollowing = user.getFollowings();
            for (User following : userFollowing) {
                if (following.getId() == loggedUserId) {
                    return (user.getLastSeen().toString());
                }
            }
        }
        return ("last seen recently");
    }

    public String birthdayForLoggedUser(User user) {
        User.Level status = user.isBirthDayVisible();
        if (status == User.Level.FOLLOWING) {
            long loggedUserId = LoggedUser.getLoggedUser().getId();
            List<User> following = user.getFollowings();
            for (User followed : following) {
                if (followed.getId() == loggedUserId) {
                    return user.getBirthday().toString();
                }
            }
            return "not visible";
        } else if (status == User.Level.ALL) {
            return user.getBirthday().toString();
        } else {
            return "not visible";
        }
    }


    public String emailForLoggedUser(User user) {
        User.Level status = user.isEmailVisible();
        if (status == User.Level.FOLLOWING) {
            long loggedUserId = LoggedUser.getLoggedUser().getId();
            List<User> following = user.getFollowings();
            for (User followed : following) {
                if (followed.getId() == loggedUserId) {
                    return user.getEmail();
                }
            }
            return "not visible";
        } else if (status == User.Level.ALL) {
            return user.getEmail();
        } else {
            return "not visible";
        }

    }

    public String phoneNumberForLoggedUser(User user) {
        User.Level status = user.isPhoneNumberVisible();
        if (status == User.Level.FOLLOWING) {
            long loggedUserId = LoggedUser.getLoggedUser().getId();
            List<User> following = user.getFollowings();
            for (User followed : following) {
                if (followed.getId() == loggedUserId) {
                    return user.getPhoneNumber();
                }
            }
            return "not visible";
        } else if (status == User.Level.ALL) {
            return user.getPhoneNumber();
        } else {
            return "not visible";
        }

    }

    public User.Level getUserNumberStatus(User loggedUser) {
        return userRepository.getById(loggedUser.getId()).isPhoneNumberVisible();
    }

    public User.Level getUserEmailStatus(User loggedUser) {
        return userRepository.getById(loggedUser.getId()).isEmailVisible();
    }

    public User.Level getUserBirthdayStatus(User loggedUser) {
        return userRepository.getById(loggedUser.getId()).isBirthDayVisible();
    }

    public void changeNumberStatus(String newStatus) {
        User.Level status = switch (newStatus) {
            case "following" -> User.Level.FOLLOWING;
            case "everybody" -> User.Level.ALL;
            default -> User.Level.NONE;
        };
        userRepository.changeNumberStatus(LoggedUser.getLoggedUser().getId(), status);
    }

    public void changeEmailStatus(String newStatus) {
        User.Level status = switch (newStatus) {
            case "following" -> User.Level.FOLLOWING;
            case "everybody" -> User.Level.ALL;
            default -> User.Level.NONE;
        };
        userRepository.changeEmailStatus(LoggedUser.getLoggedUser().getId(), status);
    }

    public void changeBirthdayStatus(String newStatus) {
        User.Level status = switch (newStatus) {
            case "following" -> User.Level.FOLLOWING;
            case "everybody" -> User.Level.ALL;
            default -> User.Level.NONE;
        };
        userRepository.changeBirthdayStatus(LoggedUser.getLoggedUser().getId(), status);
    }

    public void activateAccount(String username) {
        User deActiveUser = userRepository.getByUsername(username);
        userRepository.activateAccount(deActiveUser.getId());
    }
}
