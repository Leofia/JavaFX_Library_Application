package lib;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PendingListDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libPU");

    public void addPendingRequest(PendingList pendingList) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pendingList);
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

    public void removePendingRequest(Long id) {
        EntityManager em = emf.createEntityManager();
         try {
             em.getTransaction().begin();
             PendingList pendingList = em.find(PendingList.class, id);
             if (pendingList != null) {
                 em.remove(pendingList);
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


    public List<PendingList> getPendingRequestsForUser(User user) {
         EntityManager em = emf.createEntityManager();
         try {
            TypedQuery<PendingList> query = em.createQuery(
                    "SELECT pl FROM PendingList pl WHERE pl.receiver = :user", PendingList.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

      public boolean hasPendingRequestFromSender(User sender, User receiver) {
        EntityManager em = emf.createEntityManager();
        try {
           TypedQuery<Long> query = em.createQuery(
                   "SELECT COUNT(pl) FROM PendingList pl WHERE pl.sender = :sender AND pl.receiver = :receiver", Long.class);
            query.setParameter("sender", sender);
            query.setParameter("receiver", receiver);
             return query.getSingleResult() > 0;
        }finally {
            em.close();
        }

    }

       public PendingList findPendingRequestById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(PendingList.class, id);
        } finally {
            em.close();
        }
    }
}