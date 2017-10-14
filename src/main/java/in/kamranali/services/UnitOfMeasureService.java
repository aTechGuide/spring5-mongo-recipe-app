package in.kamranali.services;

import java.util.Set;

import in.kamranali.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {

	Flux<UnitOfMeasureCommand> listAllUoms();

}
