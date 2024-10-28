package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.NonNull;

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
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	/**
	 *
	 * @param request - El request
	 * @return
	 */
	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person();

		person.setIdentification(Integer.parseInt(request.getDni()));
		person.setFirstName(request.getFirstName());
		person.setLastName(request.getLastName());
		person.setGender(createGender(request.getSex()));
		person.setAge(Integer.parseInt(request.getAge()));

		return person;

	}

	private Gender createGender(String sex){
		return sex.equals("M") ? Gender.MALE : (sex.equals("F") ? Gender.FEMALE : null);
	}

}
