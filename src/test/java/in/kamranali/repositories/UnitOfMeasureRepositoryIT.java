package in.kamranali.repositories;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import in.kamranali.bootstrap.RecipeBootstrap;
import in.kamranali.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;
	
	@Autowired
    CategoryRepository categoryRepository;

    @Autowired
	RecipeRepository recipeRepository;
    
    @Before
    public void setUp() throws Exception {

        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);

        recipeBootstrap.onApplicationEvent(null);
    }

	
	@Test
	public void testFindByDescriptionTeaspoon() {
		Optional<UnitOfMeasure> UOMoptional = unitOfMeasureRepository.findByDescription("Teaspoon");
		assertEquals("Teaspoon", UOMoptional.get().getDescription());
	}
	
	@Test
	public void testFindByDescriptionCup() {
		Optional<UnitOfMeasure> UOMoptional = unitOfMeasureRepository.findByDescription("Cup");
		assertEquals("Cup", UOMoptional.get().getDescription());
	}

}
