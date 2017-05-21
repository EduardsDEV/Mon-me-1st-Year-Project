package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Privilege;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Chris on 18-May-17.
 */
public interface PrivilegeRepository extends CrudRepository<Privilege, Long>{

    Privilege findByName(String name);
}
