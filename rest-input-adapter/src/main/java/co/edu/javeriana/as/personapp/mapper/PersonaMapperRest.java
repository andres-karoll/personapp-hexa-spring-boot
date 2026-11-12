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
				person.getAge() != null ? person.getAge().toString() : "", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person();
		person.setIdentification(validateIdentification(request.getDni()));
		person.setFirstName(validateRequired(request.getFirstName(), "firstName"));
		person.setLastName(validateRequired(request.getLastName(), "lastName"));
		person.setAge(validateAge(request.getAge()));
		person.setGender(validateGender(request.getSex()));
		return person;
	}

	private Integer validateIdentification(String dni) {
		return parseInteger(validateRequired(dni, "dni"), "dni");
	}

	private Integer validateAge(String age) {
		return age == null || age.isBlank() ? null : parseInteger(age, "age");
	}

	private Integer parseInteger(String value, String field) {
		try {
			return Integer.valueOf(value.trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(field + " must be a number: " + value);
		}
	}

	private String validateRequired(String value, String field) {
		if (value == null || value.isBlank())
			throw new IllegalArgumentException(field + " is required");
		return value;
	}

	private Gender validateGender(String sex) {
		if ("M".equalsIgnoreCase(sex))
			return Gender.MALE;
		if ("F".equalsIgnoreCase(sex))
			return Gender.FEMALE;
		throw new IllegalArgumentException("sex must be M or F: " + sex);
	}
		
}
