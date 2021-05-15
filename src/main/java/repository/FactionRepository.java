package repository;

import controllers.FactionsController;
import models.Group;
import models.LoggedUser;
import models.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import repository.utils.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class FactionRepository {
    private final static Logger log = LogManager.getLogger(FactionRepository.class);

    public void insert(Group newGroup) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(newGroup);
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

    public void addUserToBlackList(long loggedUserId, long userToBlockId) {
        //add userToBlockId to loggedUser black list.
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, loggedUserId);
            User userToBlock = em.find(User.class, userToBlockId);
            user.getBlackList().add(userToBlock);
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

    public void removeUserFromGroup(long removedUserId, int groupId) {
        //remove removedUserId from group with groupId ID.
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, removedUserId);
            Group group = em.find(Group.class, groupId);
            group.getMembers().remove(user);
            em.persist(group);
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

    public Group getFactionById(int id) {
        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            return em.find(Group.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteFaction(int id) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            String factionName = getFactionById(id).getName();
            et = em.getTransaction();
            et.begin();
            Group object = em.find(Group.class, id);
            em.remove(object);
            et.commit();
            log.info("user " + LoggedUser.getLoggedUser().getUsername() + " deleted faction: "+ factionName);

        } catch (Exception e) {
            log.error("faction " +id +" deleting failed: error:  "+ e.getMessage());
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUserFromFaction(int groupId, long userId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            Group group = em.find(Group.class, groupId);
            group.getMembers().remove(user);
            em.persist(group);
            et.commit();
            log.info("user got removed from faction");
        } catch (Exception e) {
            log.error("removing user from faction failed. error:  " + e.getMessage());
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void addUserToFaction(int groupId, long userId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            User user = em.find(User.class, userId);
            Group group = em.find(Group.class, groupId);
            group.getMembers().add(user);
            em.persist(group);
            et.commit();
            log.info("user added to faction successfully");
        } catch (Exception e) {
            log.error("adding user " + userId +" to faction " + groupId + " failed. error: " + e.getMessage());
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
