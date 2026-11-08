package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
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
		return fromAdapterToDomain(Integer.valueOf(request.getDni()), request);
	}

	public Person fromAdapterToDomain(Integer identification, PersonaRequest request) {
		Person person = new Person(
				identification,
				request.getFirstName(),
				request.getLastName(),
				toGender(request.getSex()));
		person.setAge(request.getAge() == null || request.getAge().isBlank() ? null : Integer.valueOf(request.getAge()));
		return person;
	}

	private Gender toGender(String sex) {
		if (sex == null) {
			return Gender.OTHER;
		}
		switch (sex.trim().toUpperCase()) {
		case "M":
			return Gender.MALE;
		case "F":
			return Gender.FEMALE;
		default:
			return Gender.OTHER;
		}
	}
		
}
