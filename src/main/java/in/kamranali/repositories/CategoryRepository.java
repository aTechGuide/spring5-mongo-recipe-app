package in.kamranali.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import in.kamranali.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

	Optional<Category> findByDescription(String description);
}
