package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.JpaCrudRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * An implementation of the {@code ImageRepository} interface, backed by a JPA data source.
 */
public class JpaImageRepository extends JpaCrudRepository<Image, Integer> implements ImageRepository
{

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */

    public JpaImageRepository(EntityManager entityManager)
    {
        super(entityManager, Image.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaImageRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Image.class);
    }

    /**
     * Creates a new {@link JpaImageRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaImageRepository(JpaTransaction transaction)
    {
        super(transaction, Image.class);
    }

    @Override
    public Image create(String description, String full, String thumbnail, User user)
    {
        Image image = new Image(description, full, thumbnail, user);
        getEntityManager().persist(image);
        return image;
    }

    @Override
    public List<Image> getByUser(User user)
    {
        return getEntityManager()
                .createQuery("SELECT i FROM Image i WHERE i.user = :user", Image.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Image> getByUserPaginated(User user, int pageSize, int pageNumber)
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        return getEntityManager()
                .createQuery("SELECT i FROM Image i WHERE i.user = :user", Image.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public int countByUser(User user)
    {
        return (int) (long) getEntityManager()
                .createQuery("SELECT count(i) FROM Image i WHERE i.user = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult();
    }
}