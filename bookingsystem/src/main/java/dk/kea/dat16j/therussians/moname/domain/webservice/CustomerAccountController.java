package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Account;
import dk.kea.dat16j.therussians.moname.domain.entity.Customer;
import dk.kea.dat16j.therussians.moname.domain.entity.CustomerAccount;
import dk.kea.dat16j.therussians.moname.domain.entity.Role;
import dk.kea.dat16j.therussians.moname.domain.repository.AccountRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.CustomerRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.RoleRepository;
import dk.kea.dat16j.therussians.moname.domain.security.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by eegerpoop on 5/17/2017.
 */
@Controller
@RequestMapping(path = "/customerAccount")
public class CustomerAccountController {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;

    @Autowired
    public CustomerAccountController(AccountRepository accountRepository, CustomerRepository customerRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(path = "/create")
    @ResponseBody
    public String createCustomerAccount(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam String firstName,
                                        @RequestParam String lastName,
                                        @RequestParam String phoneNumber,
                                        @RequestParam String birthday) {
        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setBirthday(LocalDate.parse(birthday));
        c.setPhoneNumber(phoneNumber);
        c = customerRepository.save(c);
        CustomerAccount ac = new CustomerAccount();

        ac.setEmail(email);
        ac.setPassword(password);
        ac.setCustomerId(c.getId());
        ac.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        accountRepository.save(ac);
        return "Saved";
    }

    @ResponseBody
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Iterable<Account> getAllCustomerAccounts() {
        return accountRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(path = "/{customerAccount}/edit")
    public String editCustomerAccount(@PathVariable(name = "customerAccount") String email,
                                      @RequestParam String password,
                                      @RequestParam String newEmail,
                                      @RequestParam String newPassword) {
        Account ac = LoginHandler.login(accountRepository, email, password);
        CustomerAccount temp;
        if (ac != null && ac instanceof CustomerAccount) {
            temp = (CustomerAccount) ac;
        } else {
            return LoginHandler.INVALID_CREDENTIALS;
        }

        temp.setEmail(newEmail);
        temp.setPassword(newPassword);

        // TODO: 19-May-17 Should the customer id be changed?
        //temp.setCustomerId(customerId);
        
        accountRepository.save(ac);
        return "Edited";
    }


}
