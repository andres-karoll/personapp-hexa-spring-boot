package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.common.validation.Entrada;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge() != null ? person.getAge() + "" : "", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		return fromAdapterToDomain(Entrada.positivo("dni", Entrada.entero("dni", request.getDni())), request);
	}

	public Person fromAdapterToDomain(Integer identification, PersonaRequest request) {
		Person person = new Person(
				Entrada.positivo("dni", identification),
				Entrada.texto("firstName", request.getFirstName(), Entrada.MAX_NOMBRE),
				Entrada.texto("lastName", request.getLastName(), Entrada.MAX_NOMBRE),
				toGender(request.getSex()));
		person.setAge(Entrada.edadOpcional("age", request.getAge()));
		return person;
	}

	/** El esquema de MariaDB solo admite M y F (ENUM('M','F')), así que el género es obligatorio. */
	private Gender toGender(String sex) {
		if (sex == null)
			throw new IllegalArgumentException("sex es obligatorio (M o F)");
		switch (sex.trim().toUpperCase()) {
		case "M":
			return Gender.MALE;
		case "F":
			return Gender.FEMALE;
		default:
			throw new IllegalArgumentException("sex debe ser M o F");
		}
	}
		
}
