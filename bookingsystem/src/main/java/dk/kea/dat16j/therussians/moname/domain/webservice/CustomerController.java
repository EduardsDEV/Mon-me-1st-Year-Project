package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Customer;
import dk.kea.dat16j.therussians.moname.domain.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


/**
 * Created by edwar on 5/9/2017.
 */
@Controller    // This means that this class is a Controller
//@RestController @Controller vs @RestController?
@RequestMapping(path = "/customers") // This means URL's start with /customers (after Application path)
public class CustomerController {
    @Autowired // This means to get the bean called treatmentRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    private CustomerRepository customerRepository;

    @RequestMapping(path = "/add") // Map ONLY GET Requests
    @ResponseBody
    public String addNewCustomer(@RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String phoneNumber,
                                 @RequestParam String birthday) { // TODO: 10-May-17 Change to LocalDate!!!
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setPhoneNumber(phoneNumber);
        c.setBirthday(LocalDate.parse(birthday));

        customerRepository.save(c);
        return "Saved";
    }
    @ResponseBody
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Iterable<Customer> getAllCustomers() {
        // This returns a JSON with the users
        return customerRepository.findAll();
    }

    @RequestMapping(path = "/{customer}/edit")
    @ResponseBody
    public String editCustomer(@PathVariable(name = "customer") long customerId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phoneNumber, @RequestParam String birthday) {

        Customer c = customerRepository.findOne(customerId);
        if(c == null){
            return "Error";
        }

        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setPhoneNumber(phoneNumber);
        c.setBirthday(LocalDate.parse(birthday));

        customerRepository.save(c);
        return "Edited";
    }
    @RequestMapping(path = "/{customer}/delete")
    @ResponseBody
    public String deleteCustomer(@PathVariable(name = "customer") long customer) {

        try{
            customerRepository.delete(customer);
            return "deleted";
        } catch(Exception e){e.printStackTrace();}
        return "Error";


    }

}
