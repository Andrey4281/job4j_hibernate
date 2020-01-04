package ru.job4j.job4j_hibernate.persistent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.job4j.job4j_hibernate.models.Item;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;

public class DaoItemHibernateWrapper implements DaoItem {
    private static final Logger LOG = LogManager.getLogger(DaoItemHibernateWrapper.class);
    private static final class Holder {
        private static final DaoItem INSTANCE = new DaoItemHibernateWrapper();
    }
    private final SessionFactory factory;

    private DaoItemHibernateWrapper() {
        factory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public static DaoItem getInstance() {
        return DaoItemHibernateWrapper.Holder.INSTANCE;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    @Override
    public Item add(Item item) {
        return this.tx(session -> {
            item.setDone(false);
            item.setCreated(new Timestamp(System.currentTimeMillis()));
            session.save(item);
            return item;
        });
    }

    @Override
    public List<Item> getAll() {
        return this.tx(session -> session.createQuery("from Item").list());
    }

    @Override
    public List<Item> getAllIsNotDone() {
        return this.tx(session -> session.createQuery("from Item where done=false").list());
    }
}
