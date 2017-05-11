package dk.kea.dat16j.therussians.moname.technicalservices;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Chris on 09-May-17.
 */
// Taken from http://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
    /*
    The answer is simple, JPA 2.1 was released before Java 8 and the Date and Time API simply did not exist at that point in time.
    Therefore the @Temporal annotation can only be applied to attributes of type java.util.Date and java.util.Calendar.
    If you want to store a LocalDate attribute in a DATE column or a LocalDateTime in a TIMESTAMP column,
    you need to define the mapping to java.sql.Date or java.sql.Timestamp yourself.
     */

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate locDate) {
        return (locDate == null ? null : Date.valueOf(locDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return (sqlDate == null ? null : sqlDate.toLocalDate());
    }
}
