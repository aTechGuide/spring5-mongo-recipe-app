package in.kamranali.domain;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "recipes" })
@Document
public class Category {

	@Id
	private String id;
	private String description;
	
	@DBRef
	private Set<Recipe> recipes;
}
