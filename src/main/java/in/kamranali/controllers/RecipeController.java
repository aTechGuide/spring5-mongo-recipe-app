package in.kamranali.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.exceptions.NotFoundException;
import in.kamranali.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RecipeController {

	private static final String RECIPE_RECIPRFORM_URL = "recipe/recipeform";
	
	private RecipeService recipeService;
	
	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/show")
	public String getIndexPage(@PathVariable String id, Model model){
		
		model.addAttribute("recipe", recipeService.findById(id));
		return "recipe/show";
	}
	
	@GetMapping("/recipe/new")
	public String newRecipe(Model model){
		
		model.addAttribute("recipe", new RecipeCommand());
		return "recipe/recipeform";
	}
	
	@GetMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model){
		
		model.addAttribute("recipe", recipeService.findCommandById(id));
		return RECIPE_RECIPRFORM_URL;
	}
	
	@PostMapping("recipe")
	public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()){
			
			bindingResult.getAllErrors().forEach(error -> log.debug(error.toString()));
			return RECIPE_RECIPRFORM_URL;
		}
		
		RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("/recipe/{id}/delete")
	public String deleteById(@PathVariable String id){
		
		log.debug("Deleting id:" + id);
		recipeService.deleteByid(id);
		return "redirect:/";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception){
		
		log.error("Handling Not found Exception: ");
		log.error(exception.getMessage());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("404error");
		modelAndView.addObject("exception", exception);
		
		return modelAndView;
	}
}
