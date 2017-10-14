package in.kamranali.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import in.kamranali.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;

import in.kamranali.commands.UnitOfMeasureCommand;
import in.kamranali.converters.UnitOfMeasureToUnitOfMeasureCommand;
import in.kamranali.repositories.UnitOfMeasureRepository;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
	
	public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		super();
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}

	@Override
	public Flux<UnitOfMeasureCommand> listAllUoms() {

		return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);

//		return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
//				.map(unitOfMeasure -> unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasure))
//				.collect(Collectors.toSet());
	}

}
