package in.kamranali.services;

import java.util.Set;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.domain.Recipe;

public interface RecipeService {

	Set<Recipe> getRecipes();
	Recipe findById(String l);
	
	RecipeCommand saveRecipeCommand(RecipeCommand command);
	RecipeCommand findCommandById(String anyLong);

	void deleteByid(String id);
}
