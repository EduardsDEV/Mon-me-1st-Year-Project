package dk.kea.dat16j.therussians.moname.domain.security;

import dk.kea.dat16j.therussians.moname.domain.entity.Account;
import dk.kea.dat16j.therussians.moname.domain.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Chris on 18-May-17.
 */
public class LoginHandler {

    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static Account login(AccountRepository accountRepository, String email, String password) {
        Account ac = accountRepository.findByEmail(email);
        if (ac == null) {
            return null; // TODO: 19-May-17 Consider throwing an error
        }
        if(!PASSWORD_ENCODER.matches(password, ac.getPassword())){
            return null;
        }/*
        if (ac.getRoles().contains(roleRepository.findByName(Role.ADMIN.getName()))) {
            AdminAccount a = new AdminAccount();
            a.setPassword(ac.getPassword());
            a.setRoles(ac.getRoles());
            a.setAccountId(ac.getAccountId());
            a.setEmail(ac.getEmail());
            return a;
        }
        if (ac.getRoles().contains(roleRepository.findByName(Role.USER.getName()))) {
            CustomerAccount a = new CustomerAccount();
            a.setPassword(ac.getPassword());
            a.setRoles(ac.getRoles());
            a.setAccountId(ac.getAccountId());
            a.setEmail(ac.getEmail());
            return a;
        }*/
        return ac;
    }

    public static enum Role {
        UNKNOWN ("UNKNOWN"),
        ADMIN("ROLE_ADMIN"),
        USER("ROLE_USER");

        private final String name;

        private Role(String s) {
            name = s;
        }

        private Role() {
            name = "";
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return name;
        }
    }
}
