package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	private PersistenceRegistry<PhoneOutputPort> registry;

	@Autowired
	private TelefonoMapperRest mapper;

	private PhoneInputPort useCase(String database) throws InvalidOptionException {
		return new PhoneUseCase(registry.resolve(database));
	}

	public List<TelefonoResponse> historial(String database) throws InvalidOptionException {
		log.info("Into historial Telefono in Input Adapter");
		return useCase(database).findAll().stream()
				.map(p -> mapper.fromDomainToAdapterRest(p, database)).collect(Collectors.toList());
	}

	public TelefonoResponse buscar(String database, String numero) throws InvalidOptionException, NoExistException {
		return mapper.fromDomainToAdapterRest(useCase(database).findOne(numero), database);
	}

	public TelefonoResponse crear(TelefonoRequest request) throws InvalidOptionException, AlreadyExistException {
		Phone phone = mapper.fromAdapterToDomain(request.getNumero(), request);
		return mapper.fromDomainToAdapterRest(useCase(request.getDatabase()).create(phone), request.getDatabase());
	}

	public TelefonoResponse editar(String database, String numero, TelefonoRequest request)
			throws InvalidOptionException, NoExistException {
		Phone phone = mapper.fromAdapterToDomain(numero, request);
		return mapper.fromDomainToAdapterRest(useCase(database).edit(numero, phone), database);
	}

	public void eliminar(String database, String numero) throws InvalidOptionException, NoExistException {
		useCase(database).drop(numero);
	}
}
