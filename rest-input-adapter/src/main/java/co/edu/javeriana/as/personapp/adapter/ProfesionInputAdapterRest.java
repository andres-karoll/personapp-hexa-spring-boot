package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	private PersistenceRegistry<ProfessionOutputPort> registry;

	@Autowired
	private ProfesionMapperRest mapper;

	private ProfessionInputPort useCase(String database) throws InvalidOptionException {
		return new ProfessionUseCase(registry.resolve(database));
	}

	public List<ProfesionResponse> historial(String database) throws InvalidOptionException {
		log.info("Into historial Profesion in Input Adapter");
		return useCase(database).findAll().stream()
				.map(p -> mapper.fromDomainToAdapterRest(p, database)).collect(Collectors.toList());
	}

	public ProfesionResponse buscar(String database, Integer id) throws InvalidOptionException, NoExistException {
		return mapper.fromDomainToAdapterRest(useCase(database).findOne(id), database);
	}

	public ProfesionResponse crear(ProfesionRequest request) throws InvalidOptionException, AlreadyExistException {
		Profession profession = mapper.fromAdapterToDomain(Integer.valueOf(request.getId()), request);
		return mapper.fromDomainToAdapterRest(useCase(request.getDatabase()).create(profession),
				request.getDatabase());
	}

	public ProfesionResponse editar(String database, Integer id, ProfesionRequest request)
			throws InvalidOptionException, NoExistException {
		Profession profession = mapper.fromAdapterToDomain(id, request);
		return mapper.fromDomainToAdapterRest(useCase(database).edit(id, profession), database);
	}

	public void eliminar(String database, Integer id) throws InvalidOptionException, NoExistException {
		useCase(database).drop(id);
	}
}
