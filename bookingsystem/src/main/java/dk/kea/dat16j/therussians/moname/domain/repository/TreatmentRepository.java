package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Treatment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris on 07-May-17.
 */
@Repository
public interface TreatmentRepository extends CrudRepository<Treatment, String>{
}
