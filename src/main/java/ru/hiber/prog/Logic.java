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

import java.util.List;

/**
 * Class Logic -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
public class Logic {

    private static final Logger LOG = LogManager.getLogger(Logic.class.getName());

    private static final Logic LOGIC = new Logic();

    private static final HiberDB DB = HiberDB.getBD();

    private static final String SAME_CAR_ERROR = "CALL POLICE THE SAME VIN BUT DIFFERENT CARS";

    private static final String USER_GROUP_NAME = "USER_RIGHTS";

    private static final String ADMIN_GROUP_NAME = "ADMIN_RIGHTS";

    private Logic() {
        CreateRights.RIGHTS_CREATOR.create();
        CarBase.LOADER.prepareCarBase();
    }

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(User.class, new UserGsonAdapter())
            .registerTypeAdapter(Engine.class, new EngGsonAdapter())
            .registerTypeAdapter(Car.class, new CarGsonApadter())
            .registerTypeAdapter(Advertisement.class, new AdvGsonAdapter())
            .registerTypeAdapter(Company.class, new CompanyGsonApadter())
            .create();

    public static Logic getLogic() {
        return LOGIC;
    }

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
        Company company = DB.getCompany(comp);
        return DB.getModels(company);
    }

}
