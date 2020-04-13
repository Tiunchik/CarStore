/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Car - data model for Car
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
@Entity
@Table(name = "Cars")
public class Car {

    @Id
    private String vin;

    private String company;

    private String model;

    private String body;

    private Timestamp made;

    @ManyToOne(fetch = FetchType.EAGER)
    private Engine engine;

    private String transmition;

    private String driveType;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Advertisement> advert = new HashSet<>();

    public void addEngine(Engine engine) {
        engine.getCars().add(this);
        this.setEngine(engine);
    }

    public void addAdvert(Advertisement adv) {
        adv.setCar(this);
        this.getAdvert().add(adv);
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getMade() {
        return made;
    }

    public void setMade(Timestamp made) {
        this.made = made;
    }


    public String getTransmition() {
        return transmition;
    }

    public void setTransmition(String transmition) {
        this.transmition = transmition;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public Set<Advertisement> getAdvert() {
        return advert;
    }

    public void setAdvert(Set<Advertisement> advert) {
        this.advert = advert;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }


}
