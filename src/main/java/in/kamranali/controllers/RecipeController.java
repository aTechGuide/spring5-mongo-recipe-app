package in.kamranali.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.exceptions.NotFoundException;
import in.kamranali.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.exceptions.TemplateInputException;

@Slf4j
@Controller
public class RecipeController {

	private static final String RECIPE_RECIPRFORM_URL = "recipe/recipeform";
	
	private RecipeService recipeService;

	private WebDataBinder webDataBinder;
	
	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		this.webDataBinder = webDataBinder;
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
		
		model.addAttribute("recipe", recipeService.findCommandById(id).block());
		return RECIPE_RECIPRFORM_URL;
	}
	
	@PostMapping("recipe")
	public String saveOrUpdate( @ModelAttribute("recipe") RecipeCommand command){

		webDataBinder.validate();
		BindingResult bindingResult = webDataBinder.getBindingResult();

		if(bindingResult.hasErrors()){

			bindingResult.getAllErrors().forEach(error -> log.debug(error.toString()));
			return RECIPE_RECIPRFORM_URL;
		}
		
		RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();
		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("/recipe/{id}/delete")
	public String deleteById(@PathVariable String id){
		
		log.debug("Deleting id:" + id);
		recipeService.deleteByid(id);
		return "redirect:/";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NotFoundException.class, TemplateInputException.class})
	public String handleNotFound(Exception exception, Model model){

		log.error("Handling Not found Exception: ");
		log.error(exception.getMessage());

		model.addAttribute("exception", exception);

		return "404error";
	}
}
