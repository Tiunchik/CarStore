/**
 * Package ru.hiber.start for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.start;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.hiber.model.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * Class FirstHiberStart - file to start and check simple operations with hibernate library
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.2
 * @since 05.04.2020
 */
public class FirstHiberStart {

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(FirstHiberStart.class.getName());

    /**
     * example of code from https://docs.jboss.org/hibernate/orm/5.4/quickstart/html_single/
     * SetUp
     *
     * @throws Exception
     */
    private static SessionFactory getFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            LOG.error("Make factory error", e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return null;
    }

    private static SessionFactory sesfactory = getFactory();

    public void create() {
        User user = new User(1);
        user.setName("Max");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setExpired(now);
        //create session t work with hibernaty
        try (Session session = sesfactory.openSession()) {

            //begin transaction, begin looks like referense to pscal, imho
            session.beginTransaction();
            session.save(user);
            //make commit of transaction, if we use get or query method, we don't have to do transaction actions
            session.getTransaction().commit();

            User temp = (User) session.get(User.class, 1);
            if (temp != null) {
                System.out.println(String.format("User has been found, user name is %s", temp.getName()));
            } else {
                System.out.println("User dosn't exist");
            }
        }
    }


    public void update() {
        User user = new User(1);

        try (Session session = sesfactory.openSession()) {
            user.setName("Semen");

            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();

            User temp = (User) session.get(User.class, 1);
            if (temp != null && temp.getName().equalsIgnoreCase("Semen")) {
                System.out.println(String.format("User has been changed, user name is %s", temp.getName()));
            } else {
                System.out.println("User dosn't exist");
            }
        }
    }

    public void delete() {
        User user = new User(1);
        try (Session session = sesfactory.openSession()) {

            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();

            User temp = (User) session.get(User.class, 1);
            if (temp == null) {
                System.out.println("User has been succesfully deleted");
            } else {
                System.out.println("User exists");
            }
        }
    }

    public void search() {
        try (Session session = sesfactory.openSession()) {

            session.beginTransaction();
            session.save(new User(1));
            session.save(new User(1));
            session.save(new User(1));
            session.getTransaction().commit();

            //name of class has to be clearly matched
            List<User> result = session.createQuery("from User").list();
            result.forEach(e -> System.out.println(e.getId()));

        }
    }

    public static void main(String[] args) throws Exception {
        FirstHiberStart start = new FirstHiberStart();
        start.create();
        start.update();
        start.delete();
        start.search();

    }
}
