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
import ru.hiber.model.Car;

import java.lang.reflect.Type;

/**
 * Class CarGsonApadter - class adapter
 * for GsonFactory for converting Car object to Json format automaticaly by Gson method toJson
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 11.04.2020
 */
public class CarGsonApadter implements JsonSerializer<Car> {
    private static final Logger LOG = LogManager.getLogger(CarGsonApadter.class.getName());

    @Override
    public JsonElement serialize(Car car, Type type, JsonSerializationContext context) {
        JsonObject answer = new JsonObject();
        answer.addProperty("vin", car.getVin());
        answer.addProperty("company", car.getCompany());
        answer.addProperty("model", car.getModel());
        answer.addProperty("body", car.getBody());
        answer.addProperty("made", car.getMade().getTime());
        answer.add("engine", context.serialize(car.getEngine()));
        answer.addProperty("transmition", car.getTransmition());
        answer.addProperty("driveType", car.getDriveType());
        return answer;
    }
}
