package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Appointment;
import dk.kea.dat16j.therussians.moname.domain.entity.Customer;
import dk.kea.dat16j.therussians.moname.domain.entity.Treatment;
import dk.kea.dat16j.therussians.moname.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Created by Chris on 10-May-17.
 */
@Controller
@RequestMapping(path = "/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @RequestMapping(path = "/add")
    public String addAppointment(@RequestParam long id,
                                 @RequestParam LocalDateTime date,
                                 @RequestParam Customer customerId,
                                 @RequestParam Treatment treatment,
                                 @RequestParam(required = false) String comment) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(id);
        appointment.setDate(date);
        appointment.setComment(comment);
        appointment.setCustomer(customerId);
        appointment.setTreatment(treatment);

        appointmentRepository.save(appointment);
        return "Saved";
    }

    @RequestMapping(path = "/all")
    public Iterable<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
