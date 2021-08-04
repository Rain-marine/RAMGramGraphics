package repository;

import models.*;
import repository.utils.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRepository {
    public List<Chat> getAllChats(long userId) {
        //all userId chat orderBy unSeen message thenOrderBy Date(if is ok, it's not necessary)
        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Chat> cq = cb.createQuery(Chat.class);
            Root<Chat> root = cq.from(Chat.class);
            Join<Chat, UserChat> chatUserJoin = root.join("userChats");

            cq.select(root);
            cq.where(cb.equal(chatUserJoin.get("user"), userId));

            cq.orderBy(cb.asc(chatUserJoin.get("hasSeen")));

            TypedQuery<Chat> typedQuery = em.createQuery(cq);

            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void addMessageToChat(long chatId, Message message) {
        //add message to chat
        //set hasSeen to false
        //UnseenCount += 1

        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Chat chat = em.find(Chat.class, chatId);
            List<UserChat> userChats = chat.getUserChats().stream()
                    .filter(it ->  it.getUser().getId() != message.getSender().getId()).collect(Collectors.toList());
            for (UserChat userChat : userChats) {
                userChat.setUnseenCount(userChat.getUnseenCount() + 1);
                userChat.setHasSeen(false);
            }
            message.setChat(chat);
            chat.getMessages().add(message);
            em.persist(chat);
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

    public void insert(Chat chat) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(chat);
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

    public void clearUnSeenCount(long chatId , long userId) {
        //set unseen count to 0
        // set hasSeen to true

        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Chat chat = em.find(Chat.class, chatId);
            UserChat userChat = chat.getUserChats().stream()
                    .filter(it -> it.getUser().getId() == userId)
                    .findAny().orElseThrow();
            userChat.setUnseenCount(0);
            userChat.setHasSeen(true);
            em.persist(chat);
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

    public Chat getById(long chatId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            return em.find(Chat.class, chatId);
        } catch (Exception e) {
            return null;
        }
    }

    public void addMemberToGroupChat(long chatId, UserChat userChat) {
        //add userChat to chatId chat
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Chat chat = em.find(Chat.class, chatId);
            chat.getUserChats().add(userChat);
            em.persist(chat);
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

    public void leaveGroup(long userId, long groupId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Chat chat = em.find(Chat.class, groupId);
            UserChat userChat = chat.getUserChats().stream().filter(it -> it.getUser().getId() == userId).findAny().orElseThrow();
            chat.getUserChats().remove(userChat);
            em.persist(chat);
            em.remove(userChat);
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
