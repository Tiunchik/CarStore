/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.awt.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class Advertisment - data model for advertisment
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
@Entity
@Table(name = "Advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User creator;

    @CreationTimestamp
    private Timestamp created;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Car car;

    @Type(type = "boolean")
    private boolean status;

    private long price;

    @Lob
    private byte[] photo;

    private String color;

    private long odomenter;

    @Type(type = "text")
    private String userComment;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> coments = new HashSet<>();

    public void addCar(Car car) {
        this.setCar(car);
        car.getAdvert().add(this);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getOdomenter() {
        return odomenter;
    }

    public void setOdomenter(int odomenter) {
        this.odomenter = odomenter;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Set<Comment> getComents() {
        return coments;
    }

    public void setComents(Set<Comment> coments) {
        this.coments = coments;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setOdomenter(long odomenter) {
        this.odomenter = odomenter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Advertisement that = (Advertisement) o;
        return id == that.id
                && created.equals(that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, created, car, odomenter);
    }
}
