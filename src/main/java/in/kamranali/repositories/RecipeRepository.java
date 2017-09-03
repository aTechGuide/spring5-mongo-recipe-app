package in.kamranali.repositories;

import org.springframework.data.repository.CrudRepository;

import in.kamranali.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String>{

}
