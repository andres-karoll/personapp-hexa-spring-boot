package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	private PersistenceRegistry<PersonOutputPort> registry;

	private PersonInputPort personInputPort;

	public void setDatabase(String database) throws InvalidOptionException {
		personInputPort = new PersonUseCase(registry.resolve(database));
	}

	public List<Person> historial() {
		return personInputPort.findAll();
	}

	public Person buscar(Integer identification) throws NoExistException {
		return personInputPort.findOne(identification);
	}

	public Person crear(Integer identification, String firstName, String lastName, Gender gender, Integer age)
			throws AlreadyExistException {
		Person person = new Person(identification, firstName, lastName, gender);
		person.setAge(age);
		return personInputPort.create(person);
	}

	public Person editar(Integer identification, String firstName, String lastName, Gender gender, Integer age)
			throws NoExistException {
		Person person = new Person(identification, firstName, lastName, gender);
		person.setAge(age);
		return personInputPort.edit(identification, person);
	}

	public Boolean eliminar(Integer identification) throws NoExistException {
		return personInputPort.drop(identification);
	}
}
