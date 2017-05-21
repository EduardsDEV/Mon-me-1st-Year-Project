package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Chris on 18-May-17.
 */
public interface AccountRepository extends CrudRepository<Account, Long>{
    Account findByEmail(String email);
}
