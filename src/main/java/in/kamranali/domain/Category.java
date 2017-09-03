package in.kamranali.domain;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "recipes" })
public class Category {

	private String id;
	private String description;
	private Set<Recipe> recipes;
}
