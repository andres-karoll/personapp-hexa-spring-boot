package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

	@Autowired
	private PersistenceRegistry<StudyOutputPort> registry;

	@Autowired
	private EstudioMapperRest mapper;

	private StudyInputPort useCase(String database) throws InvalidOptionException {
		return new StudyUseCase(registry.resolve(database));
	}

	public List<EstudioResponse> historial(String database) throws InvalidOptionException {
		log.info("Into historial Estudio in Input Adapter");
		return useCase(database).findAll().stream()
				.map(s -> mapper.fromDomainToAdapterRest(s, database)).collect(Collectors.toList());
	}

	public EstudioResponse buscar(String database, Integer dniPersona, Integer idProfesion)
			throws InvalidOptionException, NoExistException {
		return mapper.fromDomainToAdapterRest(useCase(database).findOne(dniPersona, idProfesion), database);
	}

	public EstudioResponse crear(EstudioRequest request) throws InvalidOptionException, AlreadyExistException {
		Study study = mapper.fromAdapterToDomain(Integer.valueOf(request.getDniPersona()),
				Integer.valueOf(request.getIdProfesion()), request);
		return mapper.fromDomainToAdapterRest(useCase(request.getDatabase()).create(study), request.getDatabase());
	}

	public EstudioResponse editar(String database, Integer dniPersona, Integer idProfesion, EstudioRequest request)
			throws InvalidOptionException, NoExistException {
		Study study = mapper.fromAdapterToDomain(dniPersona, idProfesion, request);
		return mapper.fromDomainToAdapterRest(useCase(database).edit(dniPersona, idProfesion, study), database);
	}

	public void eliminar(String database, Integer dniPersona, Integer idProfesion)
			throws InvalidOptionException, NoExistException {
		useCase(database).drop(dniPersona, idProfesion);
	}
}
