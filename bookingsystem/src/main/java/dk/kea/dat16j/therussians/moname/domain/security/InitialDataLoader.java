package dk.kea.dat16j.therussians.moname.domain.security;


import dk.kea.dat16j.therussians.moname.domain.entity.Privilege;
import dk.kea.dat16j.therussians.moname.domain.entity.Role;
import dk.kea.dat16j.therussians.moname.domain.repository.PrivilegeRepository;
import dk.kea.dat16j.therussians.moname.domain.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Chris on 18-May-17.
 */
// Code taken from http://www.baeldung.com/role-and-privilege-for-spring-security-registration
// and adapted to our project
@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    public static final String READ_TREATMENT = "READ_TREATMENT_PRIVILEGE";
    public static final String CREATE_TREATMENT = "CREATE_TREATMENT_PRIVILEGE";
    public static final String EDIT_TREATMENT = "EDIT_TREATMENT_PRIVILEGE";
    public static final String DELETE_TREATMENT = "DELETE_TREATMENT_PRIVILEGE";

    public static final String READ_APPOINTMENT = "READ_APPOINTMENT_PRIVILEGE";
    public static final String CREATE_APPOINTMENT = "CREATE_APPOINTMENT_PRIVILEGE";
    public static final String EDIT_APPOINTMENT = "EDIT_APPOINTMENT_PRIVILEGE";
    public static final String DELETE_APPOINTMENT = "DELETE_APPOINTMENT_PRIVILEGE";

    public static final String READ_CUSTOMER = "READ_CUSTOMER_PRIVILEGE";
    public static final String CREATE_CUSTOMER = "CREATE_CUSTOMER_PRIVILEGE";
    public static final String EDIT_CUSTOMER = "EDIT_CUSTOMER_PRIVILEGE";
    public static final String DELETE_CUSTOMER = "DELETE_CUSTOMER_PRIVILEGE";

    boolean alreadySetup = false;

    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;

    @Autowired
    public InitialDataLoader(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Privilege readTreatmentPrivilege = createPrivilegeIfNotFound(READ_TREATMENT);
        Privilege createTreatmentPrivilege = createPrivilegeIfNotFound(CREATE_TREATMENT);
        Privilege editTreatmentPrivilege = createPrivilegeIfNotFound(EDIT_TREATMENT);
        Privilege deleteTreatmentPrivilege = createPrivilegeIfNotFound(DELETE_TREATMENT);

        Privilege readAppointmentPrivilege = createPrivilegeIfNotFound(READ_APPOINTMENT);
        Privilege createAppointmentPrivilege = createPrivilegeIfNotFound(CREATE_APPOINTMENT);
        Privilege editAppointmentPrivilege = createPrivilegeIfNotFound(EDIT_APPOINTMENT);
        Privilege deleteAppointmentPrivilege = createPrivilegeIfNotFound(DELETE_APPOINTMENT);

        Privilege readCustomerPrivilege = createPrivilegeIfNotFound(READ_CUSTOMER);
        Privilege createCustomerPrivilege = createPrivilegeIfNotFound(CREATE_CUSTOMER);
        Privilege editCustomerPrivilege = createPrivilegeIfNotFound(EDIT_CUSTOMER);
        Privilege deleteCustomerPrivilege = createPrivilegeIfNotFound(DELETE_CUSTOMER);

        Collection<Privilege> adminPrivileges = Arrays.asList(readAppointmentPrivilege, readCustomerPrivilege, readTreatmentPrivilege,
                createAppointmentPrivilege, createCustomerPrivilege, createTreatmentPrivilege,
                editAppointmentPrivilege, editCustomerPrivilege, editTreatmentPrivilege,
                deleteAppointmentPrivilege, deleteCustomerPrivilege, deleteTreatmentPrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readTreatmentPrivilege, readAppointmentPrivilege, createAppointmentPrivilege,
                editAppointmentPrivilege, deleteAppointmentPrivilege, editCustomerPrivilege, deleteCustomerPrivilege));


        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
