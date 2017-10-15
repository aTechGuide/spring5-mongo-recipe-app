package in.kamranali.services;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

	Flux<Recipe> getRecipes();
	Mono<Recipe> findById(String l);
	
	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
	Mono<RecipeCommand> findCommandById(String anyLong);

	void deleteByid(String id);
}
