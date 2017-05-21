package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.Account;
import dk.kea.dat16j.therussians.moname.domain.entity.CustomerAccount;
import dk.kea.dat16j.therussians.moname.domain.repository.AccountRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.RoleRepository;
import dk.kea.dat16j.therussians.moname.domain.security.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eegerpoop on 5/17/2017.
 */
@Controller
@RequestMapping(path = "/customerAccount")
public class CustomerAccountController {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;

    @Autowired
    public CustomerAccountController(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(path = "/create")
    @ResponseBody
    public String createCustomerAccount(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam long customerId) {
        CustomerAccount ac = new CustomerAccount();

        ac.setEmail(email);
        ac.setPassword(password);
        ac.setCustomerId(customerId);

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
                                      @RequestParam String password) {
        Account ac = new LoginHandler(accountRepository, roleRepository).login(email, password);
        CustomerAccount temp;
        if (ac != null && ac instanceof CustomerAccount) {
            temp = (CustomerAccount) ac;
        } else {
            return "Invalid Customer Account";
        }

        temp.setEmail(email);
        temp.setPassword(password);
        // TODO: 19-May-17 Should the customer id be changed?
        //temp.setCustomerId(customerId); 
        ac.setPassword(password);
        
        accountRepository.save(ac);
        return "Edited";
    }


}
