package lib;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class PreviouslyBorrowedBooksDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libPU");

      public void addPreviouslyBorrowedBook(PreviouslyBorrowedBooks previouslyBorrowedBook) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(previouslyBorrowedBook);
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


    public List<PreviouslyBorrowedBooks> getPreviouslyBorrowedBooksByUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<PreviouslyBorrowedBooks> query = em.createQuery(
                    "SELECT pbb FROM PreviouslyBorrowedBooks pbb WHERE pbb.user = :user", PreviouslyBorrowedBooks.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public PreviouslyBorrowedBooks findPreviouslyBorrowedBookById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(PreviouslyBorrowedBooks.class, id);
        } finally {
            em.close();
        }
    }
}