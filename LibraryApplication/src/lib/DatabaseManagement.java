/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author İrem Serra
 */
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

public class DatabaseManagement {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libPU");

    public void addUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
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

    public void updateUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
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
        } finally {
            em.close();
        }
    }

    public void addBook(Book book) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Book findBookById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Book.class, id);
        } finally {
            em.close();
        }
    }

    public List<Book> getAllBooks() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void updateBook(Book book) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteBook(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, id);
            if (book != null) {
                em.remove(book);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void addBorrowedBook(BorrowedBooks borrowedBook) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(borrowedBook);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<BorrowedBooks> getBorrowedBooksByUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT bb FROM BorrowedBooks bb WHERE bb.user = :user", BorrowedBooks.class)
                     .setParameter("user", user)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public void returnBorrowedBook(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            BorrowedBooks borrowedBook = em.find(BorrowedBooks.class, id);
            if (borrowedBook != null) {
                borrowedBook.setReturnedDate(LocalDate.now());
                em.merge(borrowedBook);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
