package repository;

import models.Tweet;
import models.User;
import repository.utils.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class TweetRepository {

    public void insert(Tweet tweet){
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(tweet);
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

    public void like(long userId , long tweetId){
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Tweet tweet = em.find(Tweet.class, tweetId);
            tweet.getUsersWhoLiked().add(em.find(User.class, userId));
            em.persist(tweet);
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

    public List<Tweet> getUserAllTweets(long userId) {
        //tweets which: 1- tweet's user is userId  2- are in users retweet list
        // order all by date desc
        // 2 is wrong

        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
            Root<Tweet> root = cq.from(Tweet.class);

            cq.select(root);
            cq.where(cb.equal(root.get("user"), userId));

            cq.orderBy(cb.asc(root.get("tweetDateTime")));

            TypedQuery<Tweet> typedQuery = em.createQuery(cq);

            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Tweet> getAllTweets(long userId) {
        //tweets which: 1- tweet's user is userId  2- are in users retweet list
        // order all by date desc // tweet where their parentTweet is null
        // 2 is wrong

        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
            Root<Tweet> root = cq.from(Tweet.class);

            cq.select(root);
            cq.where(cb.and(cb.equal(root.get("user"), userId), cb.isNull(root.get("parentTweet"))));

            cq.orderBy(cb.desc(root.get("tweetDateTime")));

            TypedQuery<Tweet> typedQuery = em.createQuery(cq);

            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Tweet> getTopTweets(long userId) {
        // account not muted/blocked by LoggedUser
        // account not blocked LoggedUser
        // tweet not reported by LoggedUser

        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
            Root<Tweet> root = cq.from(Tweet.class);
            Join<Tweet, User> tweetUserJoin = root.join("user");

            cq.select(root);
            Predicate activeAndPublic = cb.and(cb.equal(tweetUserJoin.get("isActive"), true),
                    cb.equal(tweetUserJoin.get("isPublic"), true),cb.notEqual(tweetUserJoin.get("id"), userId));
            cq.where(cb.and(activeAndPublic, cb.isNull(root.get("parentTweet"))));

            cq.orderBy(cb.desc(root.get("tweetDateTime")));

            TypedQuery<Tweet> typedQuery = em.createQuery(cq);

            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void addComment(Tweet parentTweet, Tweet commentTweet) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            commentTweet.setParentTweet(parentTweet);
            em.persist(commentTweet);
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

    public void increaseReportCount(long tweetId) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Tweet tweet = em.find(Tweet.class, tweetId);
            tweet.setReportCounter(tweet.getReportCounter() + 1);
            em.persist(tweet);
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

    public Tweet getById(long tweetId) {
        EntityManager em = EntityManagerProvider.getEntityManager();

        try {
            return em.find(Tweet.class, tweetId);
        } catch (Exception e) {
            return null;
        }
    }


    public void delete(long tweetId) {
        //delete the tweet from DB
        emptyList(tweetId);
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Tweet object = em.find(Tweet.class,tweetId);
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

    private void emptyList(long tweetId){
        EntityManager em = EntityManagerProvider.getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            Tweet object = em.find(Tweet.class,tweetId);
            object.setUsersWhoReported(new ArrayList<>());
            object.setUsersRetweeted(new ArrayList<>());
            object.setUsersWhoLiked(new ArrayList<>());
            em.persist(object);
            et.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        }
    }
}
