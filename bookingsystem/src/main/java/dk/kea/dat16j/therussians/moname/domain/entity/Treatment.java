package dk.kea.dat16j.therussians.moname.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Chris on 07-May-17.
 */
@Entity // this is Java EE, there is a Hibernate @Entity but is deprecated? same goes for almost all the annotaions here
@Table(name = "treatments")
public class Treatment{

    //@Column(name = "treatment_name",unique = true,nullable = false)
    //@GeneratedValue
    @Id
    @Column(name = "treatment_name")
    private String name;
    @Column(name = "treatment_price")
    private double price;
    @Column(name = "treatment_duration")
    private long duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long minutes) {
        this.duration = minutes;
    }
}
