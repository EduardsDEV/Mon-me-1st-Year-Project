package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Appointment;
import dk.kea.dat16j.therussians.moname.domain.entity.Customer;
import dk.kea.dat16j.therussians.moname.domain.entity.Treatment;
import dk.kea.dat16j.therussians.moname.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * Created by Chris on 10-May-17.
 */
@Controller
@RequestMapping(path = "/appointments")
public class AppointmentController {

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    private AppointmentRepository appointmentRepository;

    @ResponseBody
    @RequestMapping(path = "/add")
    public String addAppointment(@RequestParam long id,
                                 @RequestBody LocalDateTime date,
                                 @RequestBody Customer customer,
                                 @RequestBody Treatment treatment,
                                 @RequestParam(required = false) String comment) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(id);
        appointment.setDate(date);
        appointment.setComment(comment == null || comment.isEmpty() ? null : comment);
        appointment.setCustomer(customer);
        appointment.setTreatment(treatment);

        appointmentRepository.save(appointment);
        return "Saved";
    }

    @ResponseBody
    @RequestMapping(path = "/all")
    public Iterable<Appointment> getAllAppointments() {
        Iterable<Appointment> appointments = appointmentRepository.findAll();
        appointments.forEach(appointment -> System.out.println(appointment.getDate()));
        return appointments;
    }
}
