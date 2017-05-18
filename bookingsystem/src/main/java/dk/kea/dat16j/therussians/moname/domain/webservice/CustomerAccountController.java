package dk.kea.dat16j.therussians.moname.domain.webservice;

import dk.kea.dat16j.therussians.moname.domain.entity.CustomerAccount;
import dk.kea.dat16j.therussians.moname.domain.repository.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eegerpoop on 5/17/2017.
 */
@Controller
@RequestMapping(path = "/customerAccount")
public class CustomerAccountController {

    private CustomerAccountRepository customerAccountRepository;
@Autowired
    public CustomerAccountController(CustomerAccountRepository customerAccountRepository){
        this.customerAccountRepository = customerAccountRepository;
    }

    @RequestMapping(path = "/{customerAccount}/create")
    @ResponseBody
    public String createCustomerAccount(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam long accountId ){
        CustomerAccount ac = new CustomerAccount();
        ac.setEmail(email);
        ac.setPassword(password);
        ac.setAccountId(accountId);

        customerAccountRepository.save(ac);
        return"Saved";
    }

    @ResponseBody
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Iterable<CustomerAccount> getAllCustomerAccounts(){
        return customerAccountRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(path = "/{customerAccount}/edit")
    public String editCustomerAccount(@PathVariable(name = "customerAccount") String email,
                                      @RequestParam String password){
        CustomerAccount ac = customerAccountRepository.findOne(email);
        if(ac == null){
            return "Super Error";
        }
        ac.setPassword(password);
        customerAccountRepository.save(ac);
        return "Password changed";
    }





}
