package dk.kea.dat16j.therussians.moname.domain.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by eegerpoop on 5/17/2017.
 */
@Entity
@DiscriminatorValue("CUSTOMER")
@Table(name = "customer_accounts")
public class CustomerAccount extends Account{

    @Column(name = "customer_id")
    private long customerId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
