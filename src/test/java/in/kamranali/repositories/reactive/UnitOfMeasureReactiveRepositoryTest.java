package in.kamranali.repositories.reactive;

import in.kamranali.domain.UnitOfMeasure;
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
public class UnitOfMeasureReactiveRepositoryTest {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void testFindByDescription() throws Exception {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription("Each");

        unitOfMeasureReactiveRepository.save(uom).block();

        UnitOfMeasure fetchUOM = unitOfMeasureReactiveRepository.findByDescription("Each").block();

        Assert.assertEquals("Each", fetchUOM.getDescription());
    }
}