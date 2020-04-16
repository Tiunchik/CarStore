/**
 * Package ru.hiber.model for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class RightGroup - model class for user rights, isn't used at this moment during programm execution
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
@Entity
@Table(name = "RightGroups")
public class RightGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String groupName;

    @OneToMany(mappedBy = "rights", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> userSet = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> actions = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Set<String> getActions() {
        return actions;
    }

    public void setActions(Set<String> actions) {
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RightGroup that = (RightGroup) o;
        return id == that.id
                && groupName.equals(that.groupName)
                && actions.equals(that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, actions);
    }
}
