package in.kamranali.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/*
 * Unit Test
 */
public class CategoryTest {

	Category category;
	
	@Before
	public void setUp(){
		category = new Category();
	}
	
	@Test
	public void testGetId() {
		String idValue = "4";
		category.setId(idValue);
		
		assertEquals(idValue, category.getId());
	}
}
