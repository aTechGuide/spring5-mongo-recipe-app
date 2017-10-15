package in.kamranali.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import in.kamranali.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import in.kamranali.converters.RecipeCommandToRecipe;
import in.kamranali.converters.RecipeToRecipeCommand;
import in.kamranali.domain.Recipe;
import in.kamranali.exceptions.NotFoundException;
import in.kamranali.repositories.RecipeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecipeServiceImplTest {

	private RecipeServiceImpl recipeService;
	
	@Mock
	private RecipeReactiveRepository recipeReactiveRepository;
	
	@Mock
	private RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Mock
	private RecipeCommandToRecipe recipeCommandToRecipe;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}
	
	@Test
	public void getRecipesTest() {

		Recipe recipe = new Recipe();
				
		when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(recipe));
		List<Recipe> recipes = recipeService.getRecipes().collectList().block();
		
		assertEquals(recipes.size(), 1);
//		verify(recipeRepository, times(1)).findAll();
	}
	
	@Test
	public void getRecipeByIDTest() {

		Recipe recipe1 = new Recipe();
		when(recipeReactiveRepository.findById(Mockito.anyString())).thenReturn(Mono.just(recipe1));
		Recipe recipe = recipeService.findById("1").block();
		
		assertEquals(recipe, recipe1);
		verify(recipeReactiveRepository, times(1)).findById(Mockito.anyString());
		verify(recipeReactiveRepository, never()).findAll();
	}
	
	@Test
	public void deleteByidTest() {
		
		String testID = "2";

		when(recipeReactiveRepository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
		recipeService.deleteByid(testID);
		verify(recipeReactiveRepository, times(1)).deleteById(testID);
	}
}
