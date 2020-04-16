/**
 * Package ru.hiber.prog for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hiber.adapter.*;
import ru.hiber.model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class Logic - logic class, service to servlets and work with hiberDB class
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
public class Logic {

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(Logic.class.getName());

    /**
     * create example of Logic class
     */
    private static final Logic LOGIC = new Logic();

    /**
     * link to HiberDB
     */
    private static final HiberDB DB = HiberDB.getBD();

    /**
     * error message if uses the same vin for different cars
     */
    private static final String SAME_CAR_ERROR = "CALL POLICE THE SAME VIN BUT DIFFERENT CARS";

    /**
     * name for first group of user rights
     */
    private static final String USER_GROUP_NAME = "USER_RIGHTS";

    /**
     * name for second group of user rights
     */
    private static final String ADMIN_GROUP_NAME = "ADMIN_RIGHTS";

    /**
     * private constructor, implements singleton pattern
     */
    private Logic() {
        CreateRights.RIGHTS_CREATOR.create();
        CarBase.LOADER.prepareCarBase();
    }

    /**
     * Gson, created with factory and futher used by different servlets
     */
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(User.class, new UserGsonAdapter())
            .registerTypeAdapter(Engine.class, new EngGsonAdapter())
            .registerTypeAdapter(Car.class, new CarGsonApadter())
            .registerTypeAdapter(Advertisement.class, new AdvGsonAdapter())
            .registerTypeAdapter(Company.class, new CompanyGsonApadter())
            .create();

    /**
     * static getter for logic
     *
     * @return Logic
     */
    public static Logic getLogic() {
        return LOGIC;
    }

    /**
     * static getter for Gson
     *
     * @return Gson
     */
    public Gson getGson() {
        return GSON;
    }

    public boolean addNewUser(User user) {
        var answer = false;
        if (DB.getUserByLogin(user) == null) {
            RightGroup usersGroup = DB.gerRights(USER_GROUP_NAME);
            user.setRights(usersGroup);
            DB.addNewUser(user);
            answer = true;
        }
        return answer;
    }

    public void inserFullAdv(Advertisement adv, Car car, Engine eng, User user) {
        DB.inserFullAdv(adv, car, eng, user);
    }

    public void addNewCar(Car car) {
        Car baseCar = DB.getCar(car);
        if (baseCar == null) {
            DB.addNewCar(car);
        } else {
            if (car.getModel().equalsIgnoreCase(baseCar.getModel())) {
                car.setVin(baseCar.getVin());
                DB.updateCar(car);
            } else {
                LOG.error(SAME_CAR_ERROR);
            }
        }
    }

    public User getUser(User user) {
        return DB.getUser(user);
    }

    public User getUserByLogin(User user) {
        return DB.getUserByLogin(user);
    }

    public void updateUser(User user) {
        DB.updateUser(user);
    }

    public void addNewAd(Advertisement adv) {
        DB.addNewAd(adv);
    }

    public List<Advertisement> getAdList(User user) {
        return DB.getAdList(user);
    }

    public List<Advertisement> getAll() {
        return DB.getAllList();
    }

    public void updateAdv(Advertisement adv) {
        DB.updateAdv(adv);
    }

    public void updateCar(Car car) {
        DB.updateCar(car);
    }

    public void addNewEng(Engine eng) {
        DB.addNewEng(eng);
    }

    public void updateEng(Engine eng) {
        DB.updateEng(eng);
    }

    public Advertisement getAdd(Advertisement adv) {
        return DB.getAdd(adv);
    }

    public Car getCar(Car car) {
        return DB.getCar(car);
    }

    public List<Company> companyList() {
        return DB.getCompanyList();
    }

    public List<String> getModels(Company comp) {
        Company company = DB.getCompanyByName(comp);
        return DB.getModels(company);
    }

    /**
     * prepare parameters for filter execution
     *
     * @param keys filter parameters
     * @return List of filtred advertesment
     */
    public List<Advertisement> getfilteredAdd(HashMap<String, String> keys) {
        List<String> delete = keys.keySet().stream().filter(e -> keys.get(e).equalsIgnoreCase("")).collect(Collectors.toList());
        delete.forEach(keys::remove);
        for (var e : keys.keySet()) {
            if (e.equalsIgnoreCase("photo")) {
                if (keys.get(e).equalsIgnoreCase("Has image")) {
                    keys.put("photo", " != null");
                } else {
                    keys.put("photo", " = null");
                }
            }
            if (e.equalsIgnoreCase("created")) {
                if (keys.get(e).equalsIgnoreCase("Added from yesterday")) {
                    Instant now = Instant.now();
                    now = now.minus(1, ChronoUnit.DAYS);
                    Timestamp yerstaday = Timestamp.from(now);
                    keys.put("created", ">" + yerstaday.toString());
                } else {
                    Instant now = Instant.now();
                    now = now.minus(3, ChronoUnit.DAYS);
                    Timestamp timestamp = Timestamp.from(now);
                    keys.put("created", ">" + timestamp.toString());
                }
            }
            if (e.equalsIgnoreCase("status")) {
                if (keys.get(e).equalsIgnoreCase("Sold")) {
                    keys.put("status", "= false");
                } else {
                    keys.put("status", "= true");
                }
            }
            if (e.equalsIgnoreCase("car.company")) {
                if (e.equalsIgnoreCase("car.company")) {
                    StringBuilder builder = new StringBuilder(" = ")
                            .append("'")
                            .append(keys.get(e))
                            .append("'");
                    keys.put(e, builder.toString());
                }
            }
        }
        return DB.getfilteredAdd(keys);
    }

    public byte[] getImage(Advertisement adv) {
        return DB.getImage(adv);
    }

    public void addCompany(Company comp) {
        DB.addCompany(comp);
    }
}
