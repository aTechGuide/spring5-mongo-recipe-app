package in.kamranali.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import in.kamranali.commands.IngredientCommand;
import in.kamranali.commands.UnitOfMeasureCommand;
import in.kamranali.services.IngredientService;
import in.kamranali.services.RecipeService;
import in.kamranali.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	private WebDataBinder webDataBinder;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService unitOfMeasure) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasure;
	}

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder webDataBinder){
        this.webDataBinder = webDataBinder;
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

		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){
		
		log.debug("Showing ingredients for recipe id:" + recipeId);
		
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
		return "recipe/ingredient/ingredientform";
	}
	
	@PostMapping("/recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute("ingredient") IngredientCommand command, @PathVariable String recipeId, Model model){

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(error -> log.debug(error.toString()));
            return "recipe/ingredient/ingredientform";
        }
		
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();
		log.debug("Saved recipe id:" + command.getRecipeId());
		log.debug("Saved ingredient id:" + command.getId());
		
		return "redirect:/recipe/" + recipeId + "/ingredient/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteRecipeIngredient(@PathVariable String recipeId, @PathVariable String id){
		
		log.debug("Delete ingredients for recipe id:" + recipeId);
		ingredientService.deleteById(recipeId, id).block() ;
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}

	@ModelAttribute("uomList")
    public Flux<UnitOfMeasureCommand> populateUOMList(){
	    return unitOfMeasureService.listAllUoms();
    }
}
