package co.edu.javeriana.as.personapp.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

class PersonaMapperRestTest {

	private final PersonaMapperRest personaMapperRest = new PersonaMapperRest();

	@Test
	void fromAdapterToDomainMapsAllFields() {
		PersonaRequest request = new PersonaRequest("123", "Ana", "Perez", "30", "F", "MARIA");

		Person person = personaMapperRest.fromAdapterToDomain(request);

		assertEquals(123, person.getIdentification());
		assertEquals("Ana", person.getFirstName());
		assertEquals("Perez", person.getLastName());
		assertEquals(30, person.getAge());
		assertEquals(Gender.FEMALE, person.getGender());
	}

	@Test
	void fromAdapterToDomainAcceptsLowercaseSexAndEmptyAge() {
		PersonaRequest request = new PersonaRequest("123", "Luis", "Gomez", "", "m", "MONGO");

		Person person = personaMapperRest.fromAdapterToDomain(request);

		assertEquals(Gender.MALE, person.getGender());
		assertNull(person.getAge());
	}

	@Test
	void fromAdapterToDomainRejectsInvalidSex() {
		PersonaRequest request = new PersonaRequest("123", "Alex", "Diaz", "20", "X", "MARIA");

		assertThrows(IllegalArgumentException.class, () -> personaMapperRest.fromAdapterToDomain(request));
	}

	@Test
	void fromAdapterToDomainRejectsNonNumericDni() {
		PersonaRequest request = new PersonaRequest("abc", "Ana", "Perez", "30", "F", "MARIA");

		assertThrows(IllegalArgumentException.class, () -> personaMapperRest.fromAdapterToDomain(request));
	}

	@Test
	void fromAdapterToDomainRejectsMissingFirstName() {
		PersonaRequest request = new PersonaRequest("123", null, "Perez", "30", "F", "MARIA");

		assertThrows(IllegalArgumentException.class, () -> personaMapperRest.fromAdapterToDomain(request));
	}

	@Test
	void fromDomainToAdapterRestDoesNotShowNullAge() {
		Person person = new Person(123, "Ana", "Perez", Gender.FEMALE);

		PersonaResponse response = personaMapperRest.fromDomainToAdapterRestMaria(person);

		assertEquals("", response.getAge());
		assertEquals("MariaDB", response.getDatabase());
		assertEquals("OK", response.getStatus());
	}
}
