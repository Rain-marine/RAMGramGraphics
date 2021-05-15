package repository;

import models.Message;
import models.User;
import repository.utils.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class MessageRepository {

    public void insert(Message message) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(message);
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

    public void addMessageToSavedMessage(long userId, Message message) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            user.getFavoriteMessages().add(message);
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

    public void delete(long messageId) {

    }

    public void editMessageText(long messageId, String newText) {
        //message.setText(newText);
    }
}
