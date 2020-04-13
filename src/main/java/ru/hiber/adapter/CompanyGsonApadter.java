/**
 * Package ru.hiber.adapter for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hiber.model.Company;

import java.lang.reflect.Type;

/**
 * Class CompanyGsonApadter - - class adapter
 * for GsonFactory for converting Company object to Json format automaticaly by Gson method toJson
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 12.04.2020
 */
public class CompanyGsonApadter implements JsonSerializer<Company> {

    @Override
    public JsonElement serialize(Company company, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject answer = new JsonObject();
        answer.addProperty("id", company.getId());
        answer.addProperty("name", company.getName());
        return answer;
    }
}
