package in.kamranali.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.kamranali.commands.IngredientCommand;
import in.kamranali.commands.UnitOfMeasureCommand;
import in.kamranali.services.IngredientService;
import in.kamranali.services.RecipeService;
import in.kamranali.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService unitOfMeasure) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasure;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String listIngredients(@PathVariable String recipeId, Model model){
		
		log.debug("Getting ingredient list for recipe id:" + recipeId);
		
		model.addAttribute("recipe", recipeService.findCommandById(recipeId));
		return "recipe/ingredient/list";
	}
	 
	@GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
	public String listRecipeIngredients(@PathVariable String recipeId, @PathVariable String id, Model model){
		
		log.debug("Showing ingredients for recipe id:" + recipeId);
		
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
		return "recipe/ingredient/show";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/new")
	public String newIngredient(@PathVariable String recipeId, Model model){
		
		// TODO Raise exception if Null 
		recipeService.findCommandById(recipeId);
		
		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(recipeId);
		
		// Init Uom
		ingredientCommand.setUom(new UnitOfMeasureCommand());
		
		model.addAttribute("ingredient", ingredientCommand);
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

		return "recipe/ingredient/ingredientform";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){
		
		log.debug("Showing ingredients for recipe id:" + recipeId);
		
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList());
		return "recipe/ingredient/ingredientform";
	}
	
	@PostMapping("/recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command){
		
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();
		log.debug("Saved recipe id:" + command.getRecipeId());
		log.debug("Saved ingredient id:" + command.getId());
		
		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteRecipeIngredient(@PathVariable String recipeId, @PathVariable String id){
		
		log.debug("Delete ingredients for recipe id:" + recipeId);
		ingredientService.deleteById(recipeId, id).block() ;
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
