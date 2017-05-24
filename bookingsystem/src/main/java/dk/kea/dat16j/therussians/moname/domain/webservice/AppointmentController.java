package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.*;
import dk.kea.dat16j.therussians.moname.domain.repository.*;
import dk.kea.dat16j.therussians.moname.domain.security.InitialDataLoader;
import dk.kea.dat16j.therussians.moname.domain.security.LoginHandler;
import dk.kea.dat16j.therussians.moname.technicalservices.HtmlFileLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public AppointmentController(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, TreatmentRepository treatmentRepository, AccountRepository accountRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.treatmentRepository = treatmentRepository;
        this.accountRepository = accountRepository;

    }

    private AccountRepository accountRepository;
    private AppointmentRepository appointmentRepository;
    private CustomerRepository customerRepository;
    private TreatmentRepository treatmentRepository;

    @ResponseBody
    @RequestMapping(path = "/add")
    public String addAppointment(@RequestParam(name = "datetime") String dateAndTime,
                                 @RequestParam(name = "treatment") Integer treatment,
                                 @RequestParam(required = false) String comment,
                                 @RequestParam String email,
                                 @RequestParam String password) {
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime);
        Account ac = LoginHandler.login(accountRepository, email, password);
        if (ac == null) {
            return LoginHandler.INVALID_CREDENTIALS;
        }
        boolean hasPrivilege = ac.hasPrivilege(InitialDataLoader.CREATE_APPOINTMENT);
        if (hasPrivilege) {
            try {
                Treatment desiredTreatment = treatmentRepository.findOne(treatment);

                boolean result = checkAvailableTime(dateTime, dateTime.plusMinutes(desiredTreatment.getDuration()));
                if (result == true) {

                    Appointment newAppointment = new Appointment();
                    newAppointment.setDate(dateTime);
                    newAppointment.setComment(comment == null || comment.isEmpty() ? null : comment);
                    newAppointment.setCustomer(customerRepository.findOne(((CustomerAccount) ac).getCustomerId()));
                    newAppointment.setTreatment(desiredTreatment);

                    appointmentRepository.save(newAppointment);
                } else {
                    return "Choose different time";
                }
            } catch (EmptyResultDataAccessException e) {
                return "Treatment not found";
            }
            return "Saved";
        }
        return "No privilege";
    }

    private boolean checkAvailableTime(LocalDateTime startingPoint, LocalDateTime endingPoint) {
        if (startingPoint.isBefore(LocalDateTime.now(ZoneId.of("Europe/Paris")))) { // has one case to fail: after the customer chooses a desired time, he uses a lot of time to complete the registration
            // consider adding 10 minutes extra to the current time
            return false;
        }

        List<Appointment> appointments = getAppointmentsForDate(startingPoint.toLocalDate().toString());

        for (Appointment a : appointments) {
            LocalDateTime tempStartTime = a.getDateAndTime();
            LocalDateTime tempEndTime = a.getDateAndTime().plusMinutes(a.getTreatment().getDuration());

            // Check if end time is not in the middle of a different appointment
            if (endingPoint.isAfter(tempStartTime) && endingPoint.isBefore(tempEndTime)) {
                return false;
            }

            // Check if start time is not in the middle of a different appointment
            if (startingPoint.isAfter(tempStartTime) && startingPoint.isBefore(tempEndTime)/*start time shouldn't be before end time of a different appointment*/) {
                return false;
            }
        }

        return true;
    }

    @ResponseBody
    @RequestMapping(path = "/add-guest")
    public String addGuestAppointment(@RequestParam(name = "datetime") String dateAndTime,
                                      @RequestParam(name = "firstName") String firstName,
                                      @RequestParam(name = "lastName") String lastName,
                                      @RequestParam(name = "phoneNumber") String phoneNumber,
                                      @RequestParam(name = "birthday", required = false) String birthday,
                                      @RequestParam(name = "treatment") Integer treatment,
                                      @RequestParam(required = false) String comment) {

        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime);
        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setPhoneNumber(phoneNumber);
        c.setBirthday((birthday == null || birthday.isEmpty() ? null : LocalDate.parse(birthday)));


        Treatment desiredTreatment = treatmentRepository.findOne(treatment);
        boolean result = checkAvailableTime(dateTime, dateTime.plusMinutes(desiredTreatment.getDuration()));
        if (result == true) {
            c = customerRepository.save(c);
            Appointment newGuestAppointment = new Appointment();
            newGuestAppointment.setDate(dateTime);
            newGuestAppointment.setCustomer(c);
            newGuestAppointment.setTreatment(desiredTreatment);
            newGuestAppointment.setComment(comment == null || comment.isEmpty() ? null : comment);

            appointmentRepository.save(newGuestAppointment);
            return "Saved";
        } else {
            return "Choose different time";
        }
    }

    @ResponseBody
    @RequestMapping(path = "/all")
    public Iterable<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(path = "/{appointment}/delete")
    public String deleteAppointment(@PathVariable(name = "appointment") long appointmentId,
                                    @RequestParam String email,
                                    @RequestParam String password) {
        Account ac = LoginHandler.login(accountRepository, email, password);
        if (ac == null) {
            return LoginHandler.INVALID_CREDENTIALS;
        }
        boolean hasPrivilege = ac.hasPrivilege(InitialDataLoader.DELETE_APPOINTMENT);
        if (hasPrivilege) {
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            appointmentRepository.delete(appointment);
            return "Appointment Deleted";
        } else {
            return "No Privileges";
        }

    }
    // TODO: 16-May-17 Check if appointment is deleted from Customer's list and Treatment's list


    @ResponseBody
    @RequestMapping(path = "/{appointmentId}/edit")
    public String editAppointment(@PathVariable(name = "appointmentId") long appointmentId,
                                  @RequestParam(name = "datetime") String dateAndTime,
                                  @RequestParam(name = "customer") long customerId,
                                  @RequestParam(name = "treatment") Integer treatment,
                                  @RequestParam(required = false) String comment,
                                  @RequestParam String email,
                                  @RequestParam String password) {
        Account ac = LoginHandler.login(accountRepository, email, password);
        if (ac == null) {
            return LoginHandler.INVALID_CREDENTIALS;
        }
        boolean hasPrivilege = ac.hasPrivilege(InitialDataLoader.EDIT_APPOINTMENT);
        // TODO: 5/22/2017 Check if the appointment belongs to the customer
        // TODO: 5/22/2017 Do so that the checkAvailable doesn't interfer with this appointment
        if (hasPrivilege) {
            Appointment a = appointmentRepository.findOne(appointmentId);
            if (a == null) {
                return "Error";
            } else {
                //appointmentRepository.delete(appointmentId);
                LocalDateTime dateTime = LocalDateTime.parse(dateAndTime);
                a.setDate(dateTime);
                a.setComment(comment == null || comment.isEmpty() ? null : comment);
                a.setCustomer(customerRepository.findOne(customerId));
                a.setTreatment(treatmentRepository.findOne(treatment));
                if (checkAvailableTime(dateTime, dateTime.plusMinutes(a.getTreatment().getDuration()))) {
                    appointmentRepository.save(a);
                    return "Edited";
                } else {
                    return "Choose different time";
                }
            }
        }
        return "No privileges";
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
            temp = temp.plusDays(1);
        }

        return weekAppointments;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public void loadPage(HttpServletResponse response) throws IOException {
        String src = "src/main/resources/templates/booking.html";
        HtmlFileLoad.loadPage(response, src);
    }
}
