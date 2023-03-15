package entelect.training.spring.booking.repository;

import entelect.training.spring.booking.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingsRepository extends CrudRepository<Booking, Integer> {

    List<Booking> findByCustomerId(Integer customerID);

    Booking findByReference(String reference);
}
