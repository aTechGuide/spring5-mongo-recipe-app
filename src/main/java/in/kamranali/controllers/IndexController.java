package in.kamranali.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import in.kamranali.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

	private RecipeService recipeService;
	
	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({"", "/", "/index"})
	public String getIndexPage(Model model){
		
		log.debug("I'm in Index Page");
		model.addAttribute("recipes", recipeService.getRecipes().collectList().block());
		return "index";
	}
}
