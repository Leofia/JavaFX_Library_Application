    package lib;

    import java.time.LocalDate;
    import java.util.List;
    import javax.persistence.*;

    public class BorrowedBooksDAO {

        private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libPU");

        public void addBorrowedBook(BorrowedBooks borrowedBook) {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(borrowedBook);
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

        public List<BorrowedBooks> getBorrowedBooksByUser(User user) {
            EntityManager em = emf.createEntityManager();
            try {
                return em.createQuery("SELECT bb FROM BorrowedBooks bb WHERE bb.user = :user AND bb.returnedDate IS NULL", BorrowedBooks.class)
                        .setParameter("user", user)
                        .getResultList();
            } finally {
                em.close();
            }
        }

        public List<BorrowedBooks> getPreviouslyBorrowedBooksByUser(User user) {
            EntityManager em = emf.createEntityManager();
            try {
                return em.createQuery("SELECT bb FROM BorrowedBooks bb WHERE bb.user = :user AND bb.returnedDate IS NOT NULL", BorrowedBooks.class)
                        .setParameter("user", user)
                        .getResultList();
            } finally {
                em.close();
            }
        }

        public BorrowedBooks findBorrowedBookById(Long id) {
            EntityManager em = emf.createEntityManager();
            try {
                return em.find(BorrowedBooks.class, id);
            } finally {
                em.close();
            }
        }


            public boolean returnBorrowedBook(Long id) {
            EntityManager em = emf.createEntityManager();
            boolean isReturned = false;
            try {
                em.getTransaction().begin();
                BorrowedBooks borrowedBook = em.find(BorrowedBooks.class, id);
                if (borrowedBook != null && borrowedBook.getReturnedDate() == null) {
                    borrowedBook.setReturnedDate(LocalDate.now());

                     PreviouslyBorrowedBooks previouslyBorrowedBook = new PreviouslyBorrowedBooks();
                     previouslyBorrowedBook.setUser(borrowedBook.getUser());
                     previouslyBorrowedBook.setBook(borrowedBook.getBook());
                     previouslyBorrowedBook.setBorrowedDate(borrowedBook.getBorrowedDate());
                     previouslyBorrowedBook.setReturnedDate(LocalDate.now());
                     previouslyBorrowedBook.setOriginalBorrowedBookId(borrowedBook.getId());

                      PreviouslyBorrowedBooksDAO previouslyBorrowedBooksDAO = new PreviouslyBorrowedBooksDAO();
                      previouslyBorrowedBooksDAO.addPreviouslyBorrowedBook(previouslyBorrowedBook);

                    em.remove(borrowedBook);
                     isReturned = true;
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
            return isReturned;
        }
    }