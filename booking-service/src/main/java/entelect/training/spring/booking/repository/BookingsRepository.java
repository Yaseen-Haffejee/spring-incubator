package entelect.training.spring.booking.repository;

import entelect.training.spring.booking.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository extends CrudRepository<Booking, Integer> {
}
