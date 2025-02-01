package lib;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libPU");

    public void addUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public User findUserById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public List<User> getAllUsers() {
         EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    public  List<User> getAllUsersExcept(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.id <> :userId",User.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
        } finally {
            em.close();
        }

    }

    public void updateUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
              if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteUser(Long id) {
         EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
              if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public User findUserByUsername(String username) {
         EntityManager em = emf.createEntityManager();
         try{
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e){
             return null;
         }
        finally {
            em.close();
        }
    }


     public User findUserByUsernameAndPassword(String username,String password) {
         EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password",password);
            return query.getSingleResult();
        } catch (Exception e){
             return null;
        }finally {
            em.close();
        }
    }


    public boolean areFriends(User user1, User user2) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM User u JOIN u.friends f WHERE u = :user1 AND f = :user2", Long.class);
            query.setParameter("user1", user1);
            query.setParameter("user2", user2);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

      public List<User> findFriends(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
             TypedQuery<User> query = em.createQuery(
                    "SELECT f FROM User u JOIN u.friends f WHERE u.id = :userId", User.class);
             query.setParameter("userId", userId);
           return query.getResultList();
        } finally {
            em.close();
        }
    }

}