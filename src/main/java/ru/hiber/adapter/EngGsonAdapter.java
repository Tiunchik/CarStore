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
import ru.hiber.model.Engine;

import java.lang.reflect.Type;

/**
 * Class EngGsonAdapter -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 11.04.2020
 */
public class EngGsonAdapter implements JsonSerializer<Engine> {
    private static final Logger LOG = LogManager.getLogger(EngGsonAdapter.class.getName());

    @Override
    public JsonElement serialize(Engine eng, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject answer = new JsonObject();
        answer.addProperty("horse", eng.getHoursepowers());
        answer.addProperty("type", eng.getType());
        answer.addProperty("model", eng.getModel());
        return answer;
    }
}
