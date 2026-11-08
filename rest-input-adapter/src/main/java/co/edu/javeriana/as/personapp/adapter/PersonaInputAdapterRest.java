package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	private PersistenceRegistry<PersonOutputPort> registry;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	private PersonInputPort useCase(String database) throws InvalidOptionException {
		return new PersonUseCase(registry.resolve(database));
	}

	private PersonaResponse toResponse(Person person, String database) {
		return DatabaseOption.MARIA.name().equalsIgnoreCase(database)
				? personaMapperRest.fromDomainToAdapterRestMaria(person)
				: personaMapperRest.fromDomainToAdapterRestMongo(person);
	}

	public List<PersonaResponse> historial(String database) throws InvalidOptionException {
		log.info("Into historial PersonaEntity in Input Adapter");
		return useCase(database).findAll().stream().map(p -> toResponse(p, database)).collect(Collectors.toList());
	}

	public PersonaResponse buscar(String database, Integer id) throws InvalidOptionException, NoExistException {
		return toResponse(useCase(database).findOne(id), database);
	}

	public PersonaResponse crearPersona(PersonaRequest request) throws InvalidOptionException, AlreadyExistException {
		Person person = useCase(request.getDatabase()).create(personaMapperRest.fromAdapterToDomain(request));
		return toResponse(person, request.getDatabase());
	}

	public PersonaResponse editar(String database, Integer id, PersonaRequest request)
			throws InvalidOptionException, NoExistException {
		Person person = personaMapperRest.fromAdapterToDomain(id, request);
		return toResponse(useCase(database).edit(id, person), database);
	}

	public void eliminar(String database, Integer id) throws InvalidOptionException, NoExistException {
		useCase(database).drop(id);
	}
}
