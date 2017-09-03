package in.kamranali.services;

import in.kamranali.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
	IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
	void deleteById(String recipeId, String ingredientId);

}
