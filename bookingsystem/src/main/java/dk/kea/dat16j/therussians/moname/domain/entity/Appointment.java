package dk.kea.dat16j.therussians.moname.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Chris on 10-May-17.
 */
@Entity
public class Appointment {
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long appointmentId;
    @Column(name = "appointment_date")
    private LocalDateTime dateAndTime;
    @Column(name = "app_customer_id")
    private Customer customer;
    @Column(name = "comment")
    private String comment;
    @Column(name = "app_treatment_name")
    private Treatment treatment;

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getDate() {
        return dateAndTime;
    }

    public void setDate(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public float getTotalPrice() {
        return treatment.getPrice();
    }

    public long getDuration() {
        return treatment.getDuration();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
