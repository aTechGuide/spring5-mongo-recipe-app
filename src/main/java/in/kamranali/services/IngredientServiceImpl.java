package in.kamranali.services;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import in.kamranali.commands.IngredientCommand;
import in.kamranali.converters.IngredientCommandToIngredient;
import in.kamranali.converters.IngredientToIngredientCommand;
import in.kamranali.domain.Ingredient;
import in.kamranali.domain.Recipe;
import in.kamranali.repositories.RecipeRepository;
import in.kamranali.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IngredientServiceImpl implements IngredientService {
	
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final RecipeRepository recipeRepository;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
			RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository,
			IngredientCommandToIngredient ingredientCommandToIngredient) {
		super();
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.recipeRepository = recipeRepository;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
		
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		
		if(! recipeOptional.isPresent()){
			log.error("Recipe ID not found ID:" + recipeId);
		}
		
		Optional<IngredientCommand> ingredientCommandOptional = recipeOptional.get().getIngredients().stream()
		.filter(ingredien -> ingredien.getId().equals(ingredientId))
		.map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
		.findFirst();
		
		if(!ingredientCommandOptional.isPresent()){
			log.error("Ingredient ID not found ID:" + ingredientId);
		}
		
		return ingredientCommandOptional.get();
	}

	@Transactional
	@Override
	public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
		
		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
		
		if(! recipeOptional.isPresent()){
			log.error("Recipe ID not found ID:" + ingredientCommand.getRecipeId());
			
			return new IngredientCommand();
		}else {
			
			Recipe recipe = recipeOptional.get();
			
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
			.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
			.findFirst();
			
			if(ingredientOptional.isPresent()){
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(ingredientCommand.getDescription());
				ingredientFound.setAmount(ingredientCommand.getAmount());
				ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
						.orElseThrow(() -> new RuntimeException("UOM NOT Found")));
			} else {
				// add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}
			
			Recipe savedRecipe = recipeRepository.save(recipe);
			
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
			
			return ingredientToIngredientCommand.convert(savedIngredientOptional
					.get());
		}
	}

	@Override
	public void deleteById(String recipeId, String idToDelete) {
		
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		
		if(recipeOptional.isPresent()){
			
			Recipe recipe = recipeOptional.get();
			
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
			.filter(ingredient -> ingredient.getId().equals(idToDelete))
			.findFirst();
			
			if(ingredientOptional.isPresent()){
				
				log.debug("Found Ingredient");
				
				Ingredient ingredientToDelete = ingredientOptional.get();
				ingredientToDelete.setRecipe(null); // Nulling the relationship so that Hibernate can delte it from DB.
				
				recipe.getIngredients().remove(ingredientToDelete);
				recipeRepository.save(recipe);
			}
		}else {
			log.debug("Recipe Not Present ID: " + recipeId);
		}
		
		
	}
}
