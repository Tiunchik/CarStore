/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Class Comments - data model for comments, at this mom
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Type(type = "text")
    private String text;

    @CreationTimestamp
    private Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Advertisement advert;

    public Advertisement getAdvert() {
        return advert;
    }

    public void setAdvert(Advertisement advert) {
        this.advert = advert;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
