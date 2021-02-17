package dev.krzysztof.transactional.repositories;

import dev.krzysztof.transactional.domain.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {

}
