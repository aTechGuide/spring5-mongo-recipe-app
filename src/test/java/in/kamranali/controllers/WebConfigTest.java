package in.kamranali.controllers;

import in.kamranali.config.WebConfig;
import in.kamranali.domain.Recipe;
import in.kamranali.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.Assert.*;


/**
 * Created by bornshrewd on 16/10/17
 */
public class WebConfigTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @InjectMocks
    WebConfig webConfig;

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);

        webTestClient = WebTestClient.bindToRouterFunction(webConfig.routes(recipeService)).build();
    }
    @Test
    public void testGetRecipes() throws Exception {

        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.just());

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetRecipesWithData() throws Exception {

        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }

}