package repository;

import models.Notification;
import models.User;
import repository.utils.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class NotificationRepository {

    public void insert(Notification notification) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(notification);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteNotification(long notificationId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Notification object = em.find(Notification.class, notificationId);
            em.remove(object);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        }
    }

    public void addNewFollower(long userId, long newFollowerId) {
        //add newFollowerId to userId followers list
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            User newFollower = em.find(User.class, newFollowerId);
            user.getFollowers().add(newFollower);
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void addNewFollowing(long userId, long newFollowingId) {
        //add newFollowingId to userId following list
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            User newFollowing = em.find(User.class, newFollowingId);
            user.getFollowings().add(newFollowing);
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void removeFromFollowings(long userId, long removedFollowingId) {
        //remove removedFollowingId from userId followings list
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            User userToRemove = em.find(User.class, removedFollowingId);
            user.getFollowings().remove(userToRemove);
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void removeFromFollowers(long userId, long removedFollowerId) {
        //remove removedFollowerId from userId followers list
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            User userToRemove = em.find(User.class, removedFollowerId);
            user.getFollowers().remove(userToRemove);
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
