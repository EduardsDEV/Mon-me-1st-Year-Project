package dk.kea.dat16j.therussians.moname.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * Created by Chris on 18-May-17.
 */
@Entity
@Table(name = "accounts")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="account_type")
public abstract class Account {

    @Id
    @GeneratedValue()
    @Column(name = "account_id")
    private long accountId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Min(value = 3) // sets a minimum length for the password
    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(name = "accounts_roles", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonBackReference
    private Collection<Role> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account user = (Account) obj;
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }
}
