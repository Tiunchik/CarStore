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
import ru.hiber.model.Advertisement;

import java.lang.reflect.Type;

/**
 * Class AdGsonAdapter - class adapter
 * for GsonFactory for converting Advertisment object to Json format automaticaly by Gson method toJson
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 11.04.2020
 */
public class AdvGsonAdapter implements JsonSerializer<Advertisement> {
    private static final Logger LOG = LogManager.getLogger(AdvGsonAdapter.class.getName());

    @Override
    public JsonElement serialize(Advertisement adv, Type type, JsonSerializationContext context) {
        JsonObject answer = new JsonObject();
        answer.addProperty("id", adv.getId());
        answer.addProperty("creator", adv.getCreator().getId());
        answer.addProperty("created", adv.getCreated().getTime());
        answer.add("car", context.serialize(adv.getCar()));
        answer.addProperty("status", adv.isStatus());
        answer.addProperty("price", adv.getPrice());
        answer.addProperty("comment", adv.getUserComment());
        answer.addProperty("odometer", adv.getOdomenter());
        return answer;
    }
}
