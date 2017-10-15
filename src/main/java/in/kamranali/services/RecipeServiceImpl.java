package in.kamranali.services;

 import in.kamranali.repositories.reactive.RecipeReactiveRepository;
 import org.springframework.stereotype.Service;

 import in.kamranali.commands.RecipeCommand;
import in.kamranali.converters.RecipeCommandToRecipe;
import in.kamranali.converters.RecipeToRecipeCommand;
import in.kamranali.domain.Recipe;
 import lombok.extern.slf4j.Slf4j;
 import reactor.core.publisher.Flux;
 import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Flux<Recipe> getRecipes() {
		return recipeReactiveRepository.findAll();
	}

	@Override
	public Mono<Recipe> findById(String l) {
		return recipeReactiveRepository.findById(l);
	}

	@Override
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

		return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                .map(recipeToRecipeCommand::convert);
	}

	@Override
	public Mono<RecipeCommand> findCommandById(String id) {

		return recipeReactiveRepository.findById(id)
				.map(recipe -> {

					RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
					//enhance command object with id value
					recipeCommand.getIngredients().forEach(ingredient -> {
						ingredient.setRecipeId(recipeCommand.getId());
					});
					return recipeCommand;
				});
	}

	@Override
	public void deleteByid(String idToDelete) {
		recipeReactiveRepository.deleteById(idToDelete).block();
	}
}
