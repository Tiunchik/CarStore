/**
 * Package ru.hiber.prog for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.prog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.hiber.model.*;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class HiberDB - class for work with hibernate api
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
public enum HiberDB {
    HIBER_DB;

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(HiberDB.class.getName());

    /**
     * error message for saving operations
     */
    private static final String LOAD_ERRORS = "Error to save into DB";

    /**
     * info message for query requests to DB
     */
    private static final String NO_ENTITY_FOUND_FOR_QUERY = "No entity found for query";

    /**
     * Hibernate Session Factory
     */
    private static final SessionFactory FACTORY = createFactory();

    /**
     * Create session factory during programm uploading
     *
     * @return SessionFactory
     */
    private static SessionFactory createFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // or nonherokuhibernate.cfg.xml
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Comment.class)
                .addAnnotatedClass(Advertisement.class)
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(RightGroup.class)
                .addAnnotatedClass(Engine.class)
                .addAnnotatedClass(Company.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }

    /**
     * return Enum HIBER_DB
     *
     * @return Enum HIBER_DB
     */
    public static HiberDB getBD() {
        return HIBER_DB;
    }

    /**
     * return created SessionFactory
     *
     * @return SessionFactory
     */
    public SessionFactory getFactory() {
        return FACTORY;
    }

    /**
     * method for functional construction another methods without receiving information
     *
     * @param action Session method
     */
    public void baseAction(Consumer<Session> action) {
        Transaction tran = null;
        Session session = HIBER_DB.getFactory().openSession();
        try {
            tran = session.getTransaction();
            tran.begin();
            action.accept(session);
            tran.commit();
            session.close();
        } catch (Exception e) {
            if (tran != null) {
                tran.rollback();
            }
            LOG.error(LOAD_ERRORS, e);
            session.close();
        }
    }

    /**
     * method for functional construction another methods with receiving information
     *
     * @param action Session method and type of returning value
     */
    public <E> E baseQuaery(Function<Session, E> action) {
        E answer = null;
        Session session = HIBER_DB.getFactory().openSession();
        try {
            answer = action.apply(session);
            session.close();
        } catch (NoResultException e) {
            LOG.info(NO_ENTITY_FOUND_FOR_QUERY);
            session.close();
        }
        return answer;
    }

    /**
     * add ompany to DB
     *
     * @param comp added company
     */
    public void addCompany(Company comp) {
        baseAction(session -> session.persist(comp));
    }

    public List<Company> getCompanyList() {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Company");
            return (List<Company>) query.getResultList();
        });
    }

    /**
     * get Company object from DB by name
     *
     * @param comp Company with setted name
     * @return Full company
     */
    public Company getCompanyByName(Company comp) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Company AS a WHERE a.name= :name");
            query.setParameter("name", comp.getName());
            return (Company) query.getSingleResult();
        });
    }

    /**
     * get list of models from company
     *
     * @param comp Company
     * @return List of models of the company
     */
    public List<String> getModels(Company comp) {
        return baseQuaery(session -> session
                .get(Company.class, comp.getId())
                .getModels()
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    /**
     * add user to DB
     *
     * @param user new use without Id
     */
    public void addNewUser(User user) {
        baseAction(session -> session.persist(user));
    }

    /**
     * get user from DB by id
     *
     * @param user user with id
     * @return full user
     */
    public User getUser(User user) {
        return baseQuaery(session -> session.get(User.class, user.getId()));
    }

    /**
     * update user by id
     *
     * @param user full user with id
     */
    public void updateUser(User user) {
        baseAction(session -> session.update(user));
    }

    /**
     * search user by login
     *
     * @param user user with login
     * @return fullfilled user
     */
    public User getUserByLogin(User user) {
        return (User) baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.User AS u WHERE u.login= :login");
            query.setParameter("login", user.getLogin());
            return query.getSingleResult();
        });
    }

    /**
     * added new add to DB
     *
     * @param adv new add
     */
    public void addNewAd(Advertisement adv) {
        baseAction(session -> session.persist(adv));
    }

    /**
     * get all ads that is belonged to user
     *
     * @param user creator of ads
     * @return list of them ads
     */
    public List<Advertisement> getAdList(User user) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Advertisement AS a WHERE a.creator.id= :id");
            query.setParameter("id", user.getId());
            return (List<Advertisement>) query.getResultList();
        });
    }

    /**
     * get full list of advertisement from DB
     *
     * @return full list of advertisement
     */
    public List<Advertisement> getAllList() {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Advertisement");
            return (List<Advertisement>) query.getResultList();
        });
    }

    /**
     * update advertisement by id
     *
     * @param adv fullfilled advertisement
     */
    public void updateAdv(Advertisement adv) {
        baseAction(session -> session.update(adv));
    }

    /**
     * add new Car to DB
     *
     * @param car car without Id
     */
    public void addNewCar(Car car) {
        baseAction(session -> session.persist(car));
    }

    /**
     * update Car by id
     *
     * @param car fullfilled Car
     */
    public void updateCar(Car car) {
        baseAction(session -> session.update(car));
    }

    /**
     * add new Engine to DB
     *
     * @param eng Engine without Id
     */
    public void addNewEng(Engine eng) {
        baseAction(session -> session.persist(eng));
    }

    /**
     * update Engine by id
     *
     * @param eng fullfilled Engine
     */
    public void updateEng(Engine eng) {
        baseAction(session -> session.update(eng));
    }

    /**
     * get fullfilled advertisement by id
     *
     * @param adv advertisement with id
     * @return fullfilled advertisement
     */
    public Advertisement getAdd(Advertisement adv) {
        return baseQuaery(session -> session.get(Advertisement.class, adv.getId()));
    }

    /**
     * get fullfilled Car by id
     *
     * @param car Car with id
     * @return fullfilled Car
     */
    public Car getCar(Car car) {
        return baseQuaery(session -> session.get(Car.class, car.getVin()));
    }

    public RightGroup gerRights(String nameGroup) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.RightGroup AS r WHERE r.groupName= :group");
            query.setParameter("group", nameGroup);
            return (RightGroup) query.getSingleResult();
        });
    }

    /**
     * load fitred list of advertisement
     *
     * @param keys keys for creating request
     * @return filtred list of advertisement
     */
    public List<Advertisement> getfilteredAdd(HashMap<String, String> keys) {
        StringBuilder builder = new StringBuilder("from Advertisement as adv where ");
        keys.keySet().forEach(e -> {
            builder.append("adv.").append(e);
            builder.append(keys.get(e)).append(" and ");
        });
        String requestLine = builder.toString().substring(0, builder.toString().length() - 4);
        List<Advertisement> answer = (List<Advertisement>) baseQuaery(session -> session.createQuery(requestLine).list());
        return answer;
    }

    /**
     * get image from DB
     *
     * @param adv advertisement
     * @return byte[] with image
     */
    public byte[] getImage(Advertisement adv) {
        return baseQuaery(session -> {
            Advertisement temp = session.get(Advertisement.class, adv.getId());
            return temp.getPhoto();
        });
    }

    /**
     * insert full add in one transaction
     *
     * @param adv  advertisement
     * @param car  car for advertisement
     * @param eng  engine for car
     * @param user advertisement owber
     */
    public void inserFullAdv(Advertisement adv, Car car, Engine eng, User user) {
        Transaction tran = null;
        Session session = HIBER_DB.getFactory().openSession();
        try {
            tran = session.getTransaction();
            tran.begin();
            session.refresh(user);
            car.addEngine(eng);
            adv.addCar(car);
            user.addAdv(adv);
            tran.commit();
            session.close();
        } catch (Exception e) {
            if (tran != null) {
                tran.rollback();
            }
            LOG.error(LOAD_ERRORS, e);
            session.close();
        }
    }

    public void deleteAll() {
        baseAction(session -> {
            session.createQuery("delete from User ").executeUpdate();
        });
        baseAction(session -> {
            session.createQuery("delete from Advertisement ").executeUpdate();
        });
        baseAction(session -> {
            session.createQuery("delete from Engine ").executeUpdate();
        });
        baseAction(session -> {
            session.createQuery("delete from Car ").executeUpdate();
        });
        baseAction(session -> {
            session.createQuery("delete from Company ").executeUpdate();
        });

        baseAction(Session::clear);
        baseAction(Session::flush);
    }
}