package in.kamranali.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public class RecipeServiceImplTest {

	private RecipeServiceImpl recipeService;
	
	@Mock
	private RecipeRepository recipeRepository;
	
	@Mock
	private RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Mock
	private RecipeCommandToRecipe recipeCommandToRecipe;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}
	
	@Test
	public void getRecipesTest() {
		
		Set<Recipe> recipeSet = Stream.of(new Recipe()).collect(Collectors.toCollection(HashSet::new));
				
		when(recipeRepository.findAll()).thenReturn(recipeSet);
		Set<Recipe> recipes = recipeService.getRecipes();
		
		assertEquals(recipes.size(), 1);
//		verify(recipeRepository, times(1)).findAll();
	}
	
	@Test
	public void getRecipeByIDTest() {
		
		Optional<Recipe> recipeOptional = Optional.of(new Recipe());
				
		when(recipeRepository.findById(Mockito.anyString())).thenReturn(recipeOptional);
		Recipe recipe = recipeService.findById("1");
		
		assertEquals(recipe, recipeOptional.get());
		verify(recipeRepository, times(1)).findById(Mockito.anyString());
		verify(recipeRepository, never()).findAll();
	}
	
	@Test
	public void deleteByidTest() {
		
		String testID = "2";
		recipeService.deleteByid(testID);
		verify(recipeRepository, times(1)).deleteById(testID);
	}
	
	@Test(expected=NotFoundException.class)
	public void getRecipeByIDTestNotFound() {
		
		Optional<Recipe> recipeOptional = Optional.empty();
				
		when(recipeRepository.findById(Mockito.anyString())).thenReturn(recipeOptional);
		recipeService.findById("1");
		
		fail("Exception Expected");
	}
}
