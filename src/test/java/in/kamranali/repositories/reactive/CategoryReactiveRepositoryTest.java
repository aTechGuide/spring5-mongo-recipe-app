package in.kamranali.repositories.reactive;

import in.kamranali.domain.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRepositoryTest {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Before
    public void setUp() throws Exception {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void findByDescription() throws Exception {

        Category category = new Category();
        category.setDescription("Foo");

        categoryReactiveRepository.save(category).then().block();

        Category fetchCategory = categoryReactiveRepository.findByDescription("Foo").block();

        Assert.assertNotNull(fetchCategory.getId());
    }

}