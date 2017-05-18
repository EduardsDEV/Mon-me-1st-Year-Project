package dk.kea.dat16j.therussians.moname.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by eegerpoop on 5/17/2017.
 */
@Entity
@Table(name = "customer_accounts")
public class CustomerAccount {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "acc_customer_id")
    private long accountId;

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

    public long getAccountId() {

        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
