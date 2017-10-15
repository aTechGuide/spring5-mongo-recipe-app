package in.kamranali.services;

import java.util.Optional;

import in.kamranali.repositories.reactive.RecipeReactiveRepository;
import in.kamranali.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import in.kamranali.commands.IngredientCommand;
import in.kamranali.converters.IngredientCommandToIngredient;
import in.kamranali.converters.IngredientToIngredientCommand;
import in.kamranali.domain.Ingredient;
import in.kamranali.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;

@Slf4j
@Component
public class IngredientServiceImpl implements IngredientService {
	
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final RecipeReactiveRepository recipeReactiveRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
								 RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
								 IngredientCommandToIngredient ingredientCommandToIngredient) {
		super();
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		return recipeReactiveRepository
				.findById(recipeId)
				.flatMapIterable(Recipe::getIngredients)
				.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
				.single()
				.map(ingredient -> {
					IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
					command.setRecipeId(recipeId);
					return command;
				});
	}

	@Transactional
	@Override
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
		
		Recipe recipe = recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).block();
		
		if( recipe == null){
			log.error("Recipe ID not found ID:" + ingredientCommand.getRecipeId());
			
			return Mono.just(new IngredientCommand());
		}else {
			
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
			.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
			.findFirst();
			
			if(ingredientOptional.isPresent()){
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(ingredientCommand.getDescription());
				ingredientFound.setAmount(ingredientCommand.getAmount());
				ingredientFound.setUom(unitOfMeasureReactiveRepository.findById(ingredientCommand.getUom().getId()).block());

			} else {
				// add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}
			
			Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();
			
			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
					.findFirst();
			
			if(!savedIngredientOptional.isPresent()){
				
				// Our best GUESS
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
						.filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
						.filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(ingredientCommand.getUom().getId()))
						.findFirst();
			}
			
			//enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());
			
			return Mono.just(ingredientCommandSaved);
		}
	}

	@Override
	public Mono<Void> deleteById(String recipeId, String idToDelete) {

		log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

		Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

		if(recipe != null){

			log.debug("found recipe");
			
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
			.filter(ingredient -> ingredient.getId().equals(idToDelete))
			.findFirst();
			
			if(ingredientOptional.isPresent()){
				
				log.debug("Found Ingredient");
				
				Ingredient ingredientToDelete = ingredientOptional.get();
				ingredientToDelete.setRecipe(null); // Nulling the relationship so that Hibernate can delte it from DB.
				
				recipe.getIngredients().remove(ingredientToDelete);
				recipeReactiveRepository.save(recipe).block();
			}
		}else {
			log.debug("Recipe Not Present ID: " + recipeId);
		}

		return Mono.empty() ;
	}
}
