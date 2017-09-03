package in.kamranali.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import in.kamranali.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);
}
