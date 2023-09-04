package com.example.bookstore.repository.impl;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add book: " + book + " to the DB!", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all books from the DB!", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Book.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Can't get book with id {"
                    + id + "] from the DB!", e);
        }
    }
}
