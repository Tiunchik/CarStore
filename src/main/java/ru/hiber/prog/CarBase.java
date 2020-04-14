/**
 * Package ru.hiber.prog for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.prog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hiber.model.Company;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class CarBase - fill in data base with information about companyes and cars
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 12.04.2020
 */
public enum CarBase {
    LOADER;

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(CarBase.class.getName());

    /**
     * link to hibernate class for uploading information
     */
    private static final HiberDB DB = HiberDB.getBD();

    /**
     * error message answer
     */
    private static final String ERROR_MESSAGE = "file load exception";

    /**
     * name of resourse file, that wiil be uploaded to hiber DB
     */
    private static final String RESOURSE = "carbase.txt";

    /**
     * main method of class - take connection to RESOURSE and load all information to DB base
     */
    public void prepareCarBase() {
        List<Company> check = DB.getCompanyList();
        if (check.isEmpty()) {
            HashMap<String, Company> companyHashMap = new HashMap<>();
            URL path = CarBase.class.getClassLoader().getResource(RESOURSE);
            if (path != null) {
                Path base = null;
                try {
                    base = Paths.get(path.toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try (BufferedReader reader = Files.newBufferedReader(base, UTF_8)) {
                    reader.lines().forEach(e -> {
                        String[] input = e.split("=");
                        if (companyHashMap.containsKey(input[0])) {
                            companyHashMap.get(input[0]).getModels().add(input[1]);
                        } else {
                            Company temp = new Company();
                            temp.setName(input[0]);
                            temp.getModels().add(input[1]);
                            companyHashMap.put(input[0], temp);
                        }
                    });
                    companyHashMap.values().forEach(DB::addCompany);
                } catch (IOException e) {
                    LOG.error(ERROR_MESSAGE, e);
                }
            }
        }
    }
}
