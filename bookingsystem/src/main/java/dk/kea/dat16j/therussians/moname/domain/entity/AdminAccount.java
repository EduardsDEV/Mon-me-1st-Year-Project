package dk.kea.dat16j.therussians.moname.domain.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Chris on 19-May-17.
 */
@DiscriminatorValue("ADMIN")
@Entity
@Table(name = "admin_accounts")
public class AdminAccount extends Account {
}
