package dk.kea.dat16j.therussians.moname.domain.repository;

import dk.kea.dat16j.therussians.moname.domain.entity.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris on 10-May-17.
 */
@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}
