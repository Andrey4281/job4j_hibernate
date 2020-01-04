package ru.job4j.job4j_hibernate.persistent;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.job4j.job4j_hibernate.models.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class DaoItemHibernate implements DaoItem {
    private static final Logger LOG = LogManager.getLogger(DaoItemHibernate.class);
    private static final class Holder {
        private static final DaoItem INSTANCE = new DaoItemHibernate();
    }
    private final SessionFactory factory;

    public static DaoItem getInstance() {
        return Holder.INSTANCE;
    }

    private DaoItemHibernate() {
        factory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    @Override
    public void add(Item item) {
        Session session = factory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            item.setDone(false);
            item.setCreated(new Timestamp(System.currentTimeMillis()));
            session.save(item);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
                LOG.error(e.getMessage(), e);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> getAll() {
        return factory.openSession().createQuery("from Item").list();
    }

    @Override
    public List<Item> getAllIsNotDone() {
        return factory.openSession().createQuery("from Item where done=false").list();
    }
}
