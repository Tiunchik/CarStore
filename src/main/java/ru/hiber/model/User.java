/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Class User - data model of user
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 04.04.2020
 */
public class User {

    private int id;

    private String name;

    private Timestamp expired;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getExpired() {
        return expired;
    }

    public void setExpired(Timestamp expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && Objects.equals(name, user.name)
                && Objects.equals(expired, user.expired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, expired);
    }
}
