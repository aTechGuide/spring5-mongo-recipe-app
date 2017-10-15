package in.kamranali.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import in.kamranali.domain.Recipe;
import in.kamranali.services.RecipeService;
import reactor.core.publisher.Flux;

@Ignore
public class IndexControllerTest {

	@Mock
	private RecipeService recipeService;
	@Mock
	private Model model;

	private IndexController indexController;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		indexController = new IndexController(recipeService);
	}
	
	@Test
	// We are using a mocked servlet context (Mocked Dispatcher servlet) to test MVC controllers
	public void testMockMVC() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

		when(recipeService.getRecipes()).thenReturn(Flux.empty());

		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("index"));
	}

	@Test
	public void testGetIndexPage() {

		ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

		Recipe recipe = new Recipe();

		Set<Recipe> recipes = Stream.of(new Recipe()).collect(Collectors.toCollection(HashSet::new));
		
		when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));
		
		assertEquals("index", indexController.getIndexPage(model));
		verify(model, times(1)).addAttribute(Mockito.eq("recipes"), argumentCaptor.capture());
		verify(recipeService, times(1)).getRecipes();
		
		assertEquals(1, argumentCaptor.getValue().size());

	}

}
