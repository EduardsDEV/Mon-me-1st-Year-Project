package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris on 18-May-17.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

    Role findByName(String name);
}
