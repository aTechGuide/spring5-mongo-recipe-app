package in.kamranali.domain;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "recipe" })
@NoArgsConstructor
public class Ingredient {

	private String id;
	private String description;
	private BigDecimal amount;
	private Recipe recipe;
	private UnitOfMeasure uom;

	public Ingredient(String description, BigDecimal amount, UnitOfMeasure unitOfmeasure) {
		this.description = description;
		this.amount = amount;
		this.uom = unitOfmeasure;
	}

}
