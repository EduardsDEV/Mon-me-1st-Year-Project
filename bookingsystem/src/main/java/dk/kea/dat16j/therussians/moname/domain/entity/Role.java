package dk.kea.dat16j.therussians.moname.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Chris on 18-May-17.
 */
// Code taken from http://www.baeldung.com/role-and-privilege-for-spring-security-registration
// and adapted to our project
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "role_id")
    private long id;

    @Column(name = "role_name")
    private String name;
    @ManyToMany(mappedBy = "roles", targetEntity = Account.class)
    @JsonBackReference
    private Collection<Account> accounts;

    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public Role() {
        super();
    }

    public Role(final String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Account> getUsers() {
        return accounts;
    }

    public void setUsers(Collection<Account> accounts) {
        this.accounts = accounts;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }
}
