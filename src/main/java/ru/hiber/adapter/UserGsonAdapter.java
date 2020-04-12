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
import ru.hiber.model.User;

import java.lang.reflect.Type;

/**
 * Class UserGsonAdapter - 
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 11.04.2020
 */
public class UserGsonAdapter implements JsonSerializer<User> {
    private static final Logger LOG = LogManager.getLogger(UserGsonAdapter.class.getName());

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject answer = new JsonObject();
        answer.addProperty("id", user.getId());
        answer.addProperty("login", user.getLogin());
        answer.addProperty("password", user.getPassword());
        answer.addProperty("email", user.getEmail());
        answer.addProperty("firstName", user.getFirstName());
        answer.addProperty("secondName", user.getSecondName());
        answer.addProperty("phone", user.getPhone());
        return answer;
    }
}
