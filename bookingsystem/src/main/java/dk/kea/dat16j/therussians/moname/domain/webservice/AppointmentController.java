package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Appointment;
import dk.kea.dat16j.therussians.moname.domain.entity.Treatment;
import dk.kea.dat16j.therussians.moname.domain.repository.AppointmentRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.CustomerRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.TreatmentRepository;
import dk.kea.dat16j.therussians.moname.technicalservices.HtmlFileLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime);

        // TODO: 18-May-17 Check for opening time
        // TODO: 18-May-17 Add extra time in between appointments
        // this part can be omitted at the end, as the web page shouldn't allow to choose such dates
        if(dateTime.isBefore(LocalDateTime.now())){ // has one case to fail: after the customer chooses a desired time, he uses a lot of time to complete the registration
            // consider adding 10 minutes extra to the current time
            return "Choose a different time";
        }

        Treatment desiredTreatment = treatmentRepository.findOne(treatmentName);
        List<Appointment> appointments = getAppointmentsForDate(dateTime.toLocalDate().toString());

        LocalDateTime expectedEndTime = dateTime.plusMinutes(desiredTreatment.getDuration());

        for (Appointment a : appointments) {
            LocalDateTime tempStartTime = a.getDateAndTime();
            LocalDateTime tempEndTime = a.getDateAndTime().plusMinutes(a.getTreatment().getDuration());

            // Check if end time is not in the middle of a different appointment
            if (expectedEndTime.isAfter(tempStartTime) && expectedEndTime.isBefore(tempEndTime)) {
                return "Choose a different hour";
            }

            // Check if start time is not in the middle of a different appointment
            if (dateTime.isAfter(tempStartTime) && dateTime.isBefore(tempEndTime)/*start time shouldn't be before end time of a different appointment*/) {
                return "Choose a different hour";
            }
        }

        Appointment newAppointment = new Appointment();
        newAppointment.setDate(dateTime);
        newAppointment.setComment(comment == null || comment.isEmpty() ? null : comment);
        newAppointment.setCustomer(customerRepository.findOne(customerId));
        newAppointment.setTreatment(desiredTreatment);

        appointmentRepository.save(newAppointment);
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

    // Finds all appointments for a specific date
    @ResponseBody
    @RequestMapping(path = "/{date}/all")
    public List<Appointment> getAppointmentsForDate(@PathVariable(name = "date") String date) {
        List<Appointment> temp = new ArrayList<>();
        LocalDate dateTemp = LocalDate.parse(date);
        appointmentRepository.findAll().forEach(a -> {
                    if (a.getDateAndTime().toLocalDate().equals(dateTemp)) {
                        temp.add(a);
                    }
                }
        );
        return temp;
    }

    @RequestMapping(path = "/{date}/week")
    @ResponseBody
    public List<Appointment> getScheduleForWeek(@PathVariable(name = "date") String date) {
        LocalDate temp = LocalDate.parse(date);
        boolean wasSunday = false;
        List<Appointment> weekAppointments = new LinkedList<>();
        while (temp.getDayOfWeek().getValue() <= 7 && !wasSunday) {
            wasSunday = temp.getDayOfWeek().getValue() == 7;
            weekAppointments.addAll(getAppointmentsForDate(temp.toString()));
            temp.plusDays(1);
        }

        return weekAppointments;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public void loadPage(HttpServletResponse response) throws IOException {
        String src = "src/main/resources/templates/appointments.html";
        HtmlFileLoad.loadPage(response, src);
    }
}
