package in.kamranali.repositories;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import in.kamranali.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataJpaTest // It will bring up embedded DB and Its gonna configure Spring Data JPA
public class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;
	
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
