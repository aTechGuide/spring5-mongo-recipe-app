package in.kamranali.domain;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

	private String id = UUID.randomUUID().toString();
	private String description;
	private BigDecimal amount;
	private Recipe recipe;
	
	@DBRef
	private UnitOfMeasure uom;

	public Ingredient(String description, BigDecimal amount, UnitOfMeasure unitOfmeasure) {
		this.description = description;
		this.amount = amount;
		this.uom = unitOfmeasure;
	}

}
