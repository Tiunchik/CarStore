/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class User -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String login;

    private String password;

    private String email;

    private String firstName;

    private String secondName;

    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    private RightGroup rights;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Advertisement> advert = new HashSet<>();

    public void addAdv(Advertisement adv) {
        this.getAdvert().add(adv);
        adv.setCreator(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RightGroup getRights() {
        return rights;
    }

    public void setRights(RightGroup rights) {
        this.rights = rights;
    }

    public Set<Advertisement> getAdvert() {
        return advert;
    }

    public void setAdvert(Set<Advertisement> advert) {
        this.advert = advert;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
