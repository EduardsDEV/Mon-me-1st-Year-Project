package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris on 18-May-17.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
    Account findByEmail(String email);
}
