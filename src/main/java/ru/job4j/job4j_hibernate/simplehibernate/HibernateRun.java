package ru.job4j.job4j_hibernate.simplehibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.job4j.job4j_hibernate.models.User;

import java.util.GregorianCalendar;
import java.util.List;

public class HibernateRun {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure()
                .buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        User user = new User();
        user.setName("Andrey");
        user.setExpired(new GregorianCalendar());
        session.save(user);
        session.getTransaction().commit();

        User savedUser = session.load(User.class, 6);
        System.out.println(savedUser);

        session.beginTransaction();
        session.createQuery("update User set name='Petr' where name=:param")
                .setParameter("param", "Andrey").executeUpdate();
        session.getTransaction().commit();

        User changedUser = (User) session.createQuery("from User where id=:param")
                .setParameter("param", 6).list().get(0);
        System.out.println(changedUser);

        session.beginTransaction();
        session.createQuery("delete User where id=:param")
                .setParameter("param", 6).executeUpdate();
        session.getTransaction().commit();

        List<User> users = session.createQuery("from User").list();
        users.stream().forEach(System.out::println);

        session.close();
        factory.close();
    }
}
