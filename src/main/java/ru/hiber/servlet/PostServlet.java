/**
 * Package ru.hiber.servlet for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.largeobject.BlobInputStream;
import ru.hiber.adapter.AdvGsonAdapter;
import ru.hiber.adapter.CarGsonApadter;
import ru.hiber.adapter.EngGsonAdapter;
import ru.hiber.adapter.UserGsonAdapter;
import ru.hiber.model.*;
import ru.hiber.prog.Logic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Class PostServlet - provides actions for work with DB via POST methods
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 08.04.2020
 */
@WebServlet(urlPatterns = {"/post"})
public class PostServlet extends HttpServlet {

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(PostServlet.class.getName());

    /**
     * link to logic class
     */
    private static final Logic LOGIC = Logic.getLogic();

    /**
     * error message for json parsing
     */
    private static final String ERROR = "Parse execption in post servlet";

    /**
     * json action value for create ad operation - perform creating one ad
     */
    private static final String CREATEAD = "createad";

    /**
     * json action value for get all operation - provide full advert list
     */
    private static final String GETALLAD = "getall";

    /**
     * json action value for get user ad operation - get information about all current user adds
     */
    private static final String GETUSERAD = "getuserad";

    /**
     * json action value for get one add operation - get information about one add
     */
    private static final String GETONEADD = "getoneadd";

    /**
     * json action value for get change operation - provide information is this advert of this user or now
     */
    private static final String GETCHANGE = "getchange";

    /**
     * json action value for change status operation - changse status of advertisment (sold/none sold)
     */
    private static final String CHANGESTATUS = "changestatus";

    /**
     * json action value for get company operation - get from DB list of companies
     */
    private static final String GETCOMPANY = "getcompany";

    /**
     * json action value for get models operation - get form DB list of models
     */
    private static final String GETMODELS = "getmodels";

    /**
     * json action value for get only filtered add operation - get form DB list of filtered add
     */
    private static final String FILTERADD = "filter";

    /**
     * name of file, that wiil be created when we download file
     */
    private static final String TEMP_NAME = "temp";

    /**
     * folder for images
     */
    private static String path = "/images";

    @Override
    public void init() throws ServletException {
        super.init();
        path = getServletContext().getRealPath(path);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setCharacterEncoding("UTF-8");
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            StringBuilder fullLine = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fullLine.append(line);
            }
            JSONObject jsonData = (JSONObject) new JSONParser().parse(fullLine.toString());
            String key = (String) jsonData.get("action");

            if (key.equalsIgnoreCase(CREATEAD)) {
                User user = (User) session.getAttribute("user");
                Engine eng = createEngine(jsonData);
                Car car = createCar(jsonData);
                Advertisement adv = createAdv(jsonData);
                LOGIC.inserFullAdv(adv, car, eng, user);
                if (((String) jsonData.get("photo")).equalsIgnoreCase("yes")) {
                    saveImage(adv);
                }
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase(GETUSERAD) || key.equalsIgnoreCase(GETALLAD)) {
                User user = (User) session.getAttribute("user");
                List<Advertisement> adv;
                if (!key.equalsIgnoreCase(GETALLAD) && user != null) {
                    adv = LOGIC.getAdList(user);
                } else {
                    adv = LOGIC.getAll();
                }
                String jsonAnswer = LOGIC.getGson().toJson(adv);
                resp.setContentType("application/json");
                writer.write(jsonAnswer);
                writer.flush();
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase(GETONEADD)) {
                Advertisement adv = new Advertisement();
                adv.setId(Long.parseLong((String) jsonData.get("id")));
                adv = LOGIC.getAdd(adv);
                String jsonAnswer = LOGIC.getGson().toJson(adv);
                resp.setContentType("application/json");
                writer.write(jsonAnswer);
                writer.flush();
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase(GETCHANGE)) {
                Advertisement adv = new Advertisement();
                adv.setId(Long.parseLong((String) jsonData.get("id")));
                adv = LOGIC.getAdd(adv);
                User one = adv.getCreator();
                User user = (User) session.getAttribute("user");
                if (user != null && one.getId() == user.getId()) {
                    String jsonAnswer = LOGIC.getGson().toJson(adv);
                    resp.setContentType("application/json");
                    writer.write(jsonAnswer);
                    writer.flush();
                    resp.setStatus(200);
                } else {
                    resp.setStatus(500);
                }
            }
            if (key.equalsIgnoreCase(CHANGESTATUS)) {
                try {
                    Advertisement adv = new Advertisement();
                    adv.setId(Long.parseLong((String) jsonData.get("id")));
                    adv = LOGIC.getAdd(adv);
                    adv.setStatus(!adv.isStatus());
                    LOGIC.updateAdv(adv);
                    resp.setStatus(200);
                } catch (Exception e) {
                    LOG.error("change status exception", e);
                    resp.setStatus(500);
                }
            }
            if (key.equalsIgnoreCase(GETCOMPANY)) {
                List<Company> list = LOGIC.companyList();
                list.sort(Comparator.comparing(Company::getName));
                String out = LOGIC.getGson().toJson(list);
                resp.setContentType("application/json");
                writer.write(out);
                writer.flush();
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase(GETMODELS)) {
                Company company = new Company();
                company.setName((String) jsonData.get("company"));
                List<String> models = LOGIC.getModels(company);
                String out = LOGIC.getGson().toJson(models);
                resp.setContentType("application/json");
                writer.write(out);
                writer.flush();
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase(FILTERADD)) {
                HashMap<String, String> keys = new HashMap<>();
                for (var param : jsonData.keySet()) {
                    String temp = (String) jsonData.get(param);
                    if (temp != null && !temp.equals(FILTERADD)) {
                        keys.put((String) param, temp);
                    }
                }
                List<Advertisement> list = LOGIC.getfilteredAdd(keys);
                String out = LOGIC.getGson().toJson(list);
                resp.setContentType("application/json");
                writer.write(out);
                writer.flush();
                resp.setStatus(200);
            }
        } catch (ParseException e) {
            resp.setStatus(500);
            LOG.error(ERROR, e);
        }
    }

    /**
     * get json and create Engine objct via json invormation
     *
     * @param jsonData json data with engine parameters
     * @return Engine object
     */
    private Engine createEngine(JSONObject jsonData) {
        Engine eng = new Engine();
        eng.setHoursepowers(Integer.parseInt((String) jsonData.get("horse")));
        eng.setType((String) jsonData.get("engineType"));
        return eng;
    }

    /**
     * get json and create Engine objct via json invormation
     *
     * @param jsonData json data with Car parameters
     * @return Car object
     */
    private Car createCar(JSONObject jsonData) {
        Car car = new Car();
        car.setVin((String) jsonData.get("vin"));
        car.setCompany((String) jsonData.get("company"));
        car.setModel((String) jsonData.get("model"));
        car.setBody((String) jsonData.get("body"));
        car.setMade(new Timestamp((long) jsonData.get("made")));
        car.setTransmition((String) jsonData.get("transmition"));
        car.setDriveType((String) jsonData.get("drivertype"));
        return car;
    }

    private Advertisement createAdv(JSONObject jsonData) {
        Advertisement adv = new Advertisement();
        adv.setOdomenter(Long.parseLong((String) jsonData.get("odometer")));
        adv.setPrice(Long.parseLong((String) jsonData.get("price")));
        adv.setUserComment((String) jsonData.get("comment"));
        adv.setStatus(true);
        return adv;
    }

    private boolean saveImage(Advertisement adv) {
        var answer = false;
        File load = new File(path + File.separator + TEMP_NAME);
        try (FileInputStream in = new FileInputStream(load)) {
            byte[] temp = in.readAllBytes();
            adv.setPhoto(temp);
            LOGIC.updateAdv(adv);
            answer = true;
        } catch (IOException e) {
            LOG.error("file error", e);
        }
        return answer;

    }
}
