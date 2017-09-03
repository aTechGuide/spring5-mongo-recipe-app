package in.kamranali.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Notes {

	@Id
	private String id;
	private String recipeNotes;
}
