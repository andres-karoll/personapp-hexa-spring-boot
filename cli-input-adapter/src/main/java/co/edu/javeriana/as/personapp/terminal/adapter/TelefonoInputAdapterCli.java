package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;

@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	private PersistenceRegistry<PhoneOutputPort> registry;

	private PhoneInputPort useCase;

	public void setDatabase(String database) throws InvalidOptionException {
		useCase = new PhoneUseCase(registry.resolve(database));
	}

	public List<Phone> historial() {
		return useCase.findAll();
	}

	public Phone buscar(String numero) throws NoExistException {
		return useCase.findOne(numero);
	}

	public Phone crear(String numero, String operador, Integer duenio) throws AlreadyExistException {
		return useCase.create(build(numero, operador, duenio));
	}

	public Phone editar(String numero, String operador, Integer duenio) throws NoExistException {
		return useCase.edit(numero, build(numero, operador, duenio));
	}

	public Boolean eliminar(String numero) throws NoExistException {
		return useCase.drop(numero);
	}

	private Phone build(String numero, String operador, Integer duenio) {
		Person owner = new Person();
		owner.setIdentification(duenio);
		return new Phone(numero, operador, owner);
	}
}
