package ru.hiber.prog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.hiber.model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

public class LogicTest {

    private static final HiberDB DB = HiberDB.getBD();

    private static User user;

    private static Advertisement adv;

    private static Engine eng;

    private static Car car;

    private static Company comp;

    @Before
    public void setUP() {
        user = getUser();
        adv = getAdv();
        eng = getEngine();
        car = getCar();
        comp = getCompany();
    }

    @After
    public void deleteAll() {
        DB.deleteAll();
    }

    @Test
    public void wnehWeAddUserAndThenGetThem() {
        User user = getUser();
        user.setLogin("retro");
        DB.addNewUser(user);
        User test = DB.getUser(user);
        assertEquals(user.getId(), test.getId());
    }


    @Test
    public void whenWeTryInserFullAdvTheCheckId() {
        DB.addNewUser(user);
        DB.inserFullAdv(adv, car, eng, user);
        assertTrue(adv.getId() != 0);
        assertEquals(car.getEngine(), eng);
        assertTrue(eng.getId() != 0);
    }

    @Test
    public void whenWeTryAddNewCarTheCheck() {
        DB.addNewCar(car);
        assertEquals(car.getVin(), DB.getCar(car).getVin());
    }

    @Test
    public void whenWeTrygetUserByLogin() {
        DB.addNewUser(user);
        User temp = DB.getUserByLogin(user);
        assertEquals(user.getLogin(), temp.getLogin());
    }

    @Test
    public void whenWeTryupdateUser() {
        DB.addNewUser(user);
        user.setPassword("321");
        DB.updateUser(user);
        User temp = DB.getUser(user);
        assertEquals(user, temp);
    }

    @Test
    public void whenWeTryaddNewAd() {
        DB.addNewAd(adv);
        Advertisement temp = DB.getAdd(adv);
        assertEquals(adv.getId(), temp.getId());
    }

    @Test
    public void whenWeTrygetAdList() {
        DB.addNewUser(user);
        DB.addNewAd(adv);
        user.addAdv(adv);
        DB.updateUser(user);
        DB.updateAdv(adv);
        List<Advertisement> list = DB.getAdList(user);
        assertEquals(1, list.size());
    }

    @Test
    public void whenWeTrygetAll() {
        DB.addNewAd(adv);
        List<Advertisement> list = DB.getAllList();
        assertEquals(1, list.size());
    }

    @Test
    public void whenWeTryupdateAdv() {
        DB.addNewUser(user);
        DB.addNewAd(adv);
        user.addAdv(adv);
        DB.updateUser(user);
        DB.updateAdv(adv);
        assertNotNull(DB.getAdd(adv).getCreator());
    }

    @Test
    public void whenWeTryupdateCar() {
        DB.addNewCar(car);
        car.setTransmition("Auto");
        DB.updateCar(car);
        Car temp = DB.getCar(car);
        assertEquals("Auto", temp.getTransmition());
    }

    @Test
    public void whenWeTryaddNewEng() {
        DB.addNewEng(eng);
        assertTrue(eng.getId() != 0);
    }

    @Test
    public void whenWeTrygetAdd() {
        DB.addNewAd(adv);
        var temp = DB.getAdd(adv);
        assertEquals(adv, temp);
    }

    @Test
    public void whenWeTrygetCar() {
        DB.addNewCar(car);
        var temp = DB.getCar(car);
        assertEquals(car, temp);
    }

    @Test
    public void whenWeGetcompanyList() {
        DB.addCompany(comp);
        List<Company> list = DB.getCompanyList();
        assertTrue(list.size() > 0);
    }

    @Test
    public void whenWeGetModels() {
        DB.addCompany(comp);
        List<String> list = DB.getModels(comp);
        assertEquals(4, list.size());
    }

    private Engine getEngine() {
        Engine eng = new Engine();
        eng.setHoursepowers(280);
        eng.setType("Petrol");
        eng.setModel("1JZ-GTE");
        return eng;
    }

    private User getUser() {
        User user = new User();
        user.setLogin("Student");
        user.setPassword("123");
        user.setEmail("Stud@bk.ru");
        user.setFirstName("Alex");
        user.setSecondName("Petrov");
        return user;
    }

    private Car getCar() {
        Car car = new Car();
        Random test = new Random();
        car.setVin("VIN" + test.nextFloat());
        car.setCompany("Toyo");
        car.setModel("City car");
        car.setBody("SALOON");
        car.setDriveType("Front");
        car.setTransmition("CVT");
        Instant now = Instant.now();
        car.setMade(Timestamp.from(now));
        return car;
    }

    private Advertisement getAdv() {
        var adv = new Advertisement();
        adv.setStatus(true);
        adv.setPrice(650000);
        adv.setOdomenter(5600);
        return adv;
    }

    private Company getCompany() {
        var comp = new Company();
        comp.setName("JDM");
        comp.setModels(Set.of("Mark2", "Silvia", "Skyline", "Supra"));
        return comp;
    }


}