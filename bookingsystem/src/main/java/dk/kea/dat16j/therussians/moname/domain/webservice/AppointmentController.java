package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Appointment;
import dk.kea.dat16j.therussians.moname.domain.repository.AppointmentRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.CustomerRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.TreatmentRepository;
import dk.kea.dat16j.therussians.moname.technicalservices.HtmlFileLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Chris on 10-May-17.
 */
@Controller
@RequestMapping(path = "/appointments")
public class AppointmentController {

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, TreatmentRepository treatmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.treatmentRepository = treatmentRepository;
    }

    private AppointmentRepository appointmentRepository;
    private CustomerRepository customerRepository;
    private TreatmentRepository treatmentRepository;

    @ResponseBody
    @RequestMapping(path = "/add")
    public String addAppointment(@RequestParam(name = "datetime") String dateAndTime,
                                 @RequestParam(name = "customer") long customerId,
                                 @RequestParam(name = "treatment") String treatmentName,
                                 @RequestParam(required = false) String comment) {
        Appointment appointment = new Appointment();

        appointment.setDate(LocalDateTime.parse(dateAndTime));
        appointment.setComment(comment == null || comment.isEmpty() ? null : comment);
        appointment.setCustomer(customerRepository.findOne(customerId));
        appointment.setTreatment(treatmentRepository.findOne(treatmentName));

        appointmentRepository.save(appointment);
        return "Saved";
    }

    @ResponseBody
    @RequestMapping(path = "/all")
    public Iterable<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(path = "/{appointment}/delete")
    public String deleteAppointment(@PathVariable(name = "appointment") long appointmentId) {
        appointmentRepository.delete(appointmentId);
        // TODO: 16-May-17 Check if appointment is deleted from Customer's list and Treatment's list
        return "Deleted";
    }

    @ResponseBody
    @RequestMapping(path = "/{appointmentId}/edit")
    public String editAppointment(@PathVariable(name = "appointmentId") long appointmentId,
                                  @RequestParam(name = "datetime") String dateAndTime,
                                  @RequestParam(name = "customer") long customerId,
                                  @RequestParam(name = "treatment") String treatmentName,
                                  @RequestParam(required = false) String comment) {

        Appointment appointment = new Appointment();

        appointment.setAppointmentId(appointmentId);
        appointment.setDate(LocalDateTime.parse(dateAndTime));
        appointment.setComment(comment == null || comment.isEmpty() ? null : comment);
        appointment.setCustomer(customerRepository.findOne(customerId));
        appointment.setTreatment(treatmentRepository.findOne(treatmentName));

        appointmentRepository.save(appointment);

        return "Edited";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public void loadPage(HttpServletResponse response) throws IOException {
        String src = "src/main/resources/templates/appointments.html";
        HtmlFileLoad.loadPage(response, src);
    }
}
