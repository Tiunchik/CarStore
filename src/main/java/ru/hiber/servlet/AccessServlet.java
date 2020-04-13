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
import ru.hiber.adapter.AdvGsonAdapter;
import ru.hiber.adapter.CarGsonApadter;
import ru.hiber.adapter.EngGsonAdapter;
import ru.hiber.adapter.UserGsonAdapter;
import ru.hiber.model.Advertisement;
import ru.hiber.model.Car;
import ru.hiber.model.Engine;
import ru.hiber.model.User;
import ru.hiber.prog.Logic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class AccessServlet - containes post method for registration, login and logout actions
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 11.04.2020
 */
@WebServlet(urlPatterns = {"/access"})
public class AccessServlet extends HttpServlet {

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(AccessServlet.class.getName());

    /**
     * link to logic class
     */
    private static final Logic LOGIC = Logic.getLogic();

    /**
     * json action value for login operation
     */
    private static final String LOGIN = "login";

    /**
     * json action value for logout operation
     */
    private static final String LOGOUT = "logout";

    /**
     * json action value for registration operation
     */
    private static final String REG = "registration";

    /**
     * json action value for rights operation - isn't used during programm execution
     */
    private static final String RIGHTS = "rights";

    /**
     * json action value for sesscion check operation operation
     */
    private static final String SESCHECK = "sessioncheck";

    /**
     * error message for json parsing
     */
    private static final String ERROR = "Parse execption in access servlet";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            StringBuilder fullLine = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fullLine.append(line);
            }
            JSONObject jsonData = (JSONObject) new JSONParser().parse(fullLine.toString());
            String key = (String) jsonData.get("action");
            if (key.equalsIgnoreCase(LOGOUT)) {
                session.setAttribute("user", null);
                User user = (User) session.getAttribute("user");
            }
            if (key.equalsIgnoreCase(LOGIN)) {
                User user = new User();
                user.setLogin((String) jsonData.get("login"));
                user = LOGIC.getUserByLogin(user);
                if (user != null && user.getPassword().equalsIgnoreCase((String) jsonData.get("password"))) {
                    session.setAttribute("user", user);
                    String jsonAnswer = LOGIC.getGson().toJson(user);
                    writer.write(jsonAnswer);
                    writer.flush();
                    resp.setStatus(200);
                } else {
                    resp.setStatus(500);
                }
            }
            if (key.equalsIgnoreCase(REG)) {
                User user = createUser(jsonData);
                if (LOGIC.addNewUser(user)) {
                    session.setAttribute("user", user);
                } else {
                    resp.setStatus(500);
                }
            }
            if (key.equalsIgnoreCase(SESCHECK)) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    String jsonAnswer = LOGIC.getGson().toJson(user);
                    writer.write(jsonAnswer);
                    writer.flush();
                    resp.setStatus(200);
                } else {
                    resp.setStatus(400);
                }
            }
        } catch (ParseException e) {
            LOG.error(ERROR, e);
        }
    }

    private User createUser(JSONObject jsonData) {
        User user = new User();
        user.setLogin((String) jsonData.get("login"));
        user.setPassword((String) jsonData.get("password"));
        user.setEmail((String) jsonData.get("email"));
        user.setFirstName((String) jsonData.get("name"));
        user.setSecondName((String) jsonData.get("surname"));
        user.setPhone((String) jsonData.get("phone"));
        return user;
    }
}
