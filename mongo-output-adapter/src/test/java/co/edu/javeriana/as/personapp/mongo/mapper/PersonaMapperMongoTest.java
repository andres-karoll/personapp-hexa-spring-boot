package co.edu.javeriana.as.personapp.mongo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

class PersonaMapperMongoTest {

	private PersonaMapperMongo personaMapperMongo;

	@BeforeEach
	void setUp() {
		personaMapperMongo = new PersonaMapperMongo();
		EstudiosMapperMongo estudiosMapperMongo = new EstudiosMapperMongo();
		TelefonoMapperMongo telefonoMapperMongo = new TelefonoMapperMongo();
		ProfesionMapperMongo profesionMapperMongo = new ProfesionMapperMongo();
		ReflectionTestUtils.setField(personaMapperMongo, "estudiosMapperMongo", estudiosMapperMongo);
		ReflectionTestUtils.setField(personaMapperMongo, "telefonoMapperMongo", telefonoMapperMongo);
		ReflectionTestUtils.setField(estudiosMapperMongo, "personaMapperMongo", personaMapperMongo);
		ReflectionTestUtils.setField(estudiosMapperMongo, "profesionMapperMongo", profesionMapperMongo);
		ReflectionTestUtils.setField(telefonoMapperMongo, "personaMapperMongo", personaMapperMongo);
		ReflectionTestUtils.setField(profesionMapperMongo, "estudiosMapperMongo", estudiosMapperMongo);
	}

	@Test
	void fromAdapterToDomainWithStudiesAndPhonesDoesNotRecurse() {
		PersonaDocument persona = new PersonaDocument(123, "Ana", "Perez", "F", 30, null, null);
		ProfesionDocument profesion = new ProfesionDocument(1, "Ingeniero", "Sistemas", null);
		EstudiosDocument estudio = new EstudiosDocument("123-1", persona, profesion, LocalDate.of(2020, 1, 15),
				"Javeriana");
		profesion.setEstudios(List.of(estudio));
		TelefonoDocument telefono = new TelefonoDocument("3001234567", "Claro", persona);
		persona.setEstudios(List.of(estudio));
		persona.setTelefonos(List.of(telefono));

		Person person = personaMapperMongo.fromAdapterToDomain(persona);

		assertEquals(Gender.FEMALE, person.getGender());
		Study study = person.getStudies().get(0);
		assertEquals(123, study.getPerson().getIdentification());
		assertEquals(1, study.getProfession().getIdentification());
		assertEquals(LocalDate.of(2020, 1, 15), study.getGraduationDate());
		assertNull(study.getPerson().getStudies());
		assertNull(study.getProfession().getStudies());
		Phone phone = person.getPhoneNumbers().get(0);
		assertEquals(123, phone.getOwner().getIdentification());
		assertNull(phone.getOwner().getPhoneNumbers());
	}

	@Test
	void fromDomainToAdapterWithStudiesAndPhonesDoesNotRecurse() {
		Person person = new Person(123, "Luis", "Gomez", Gender.MALE);
		Profession profession = new Profession(1, "Ingeniero");
		Study study = new Study(person, profession);
		profession.setStudies(List.of(study));
		person.setStudies(List.of(study));
		person.setPhoneNumbers(List.of(new Phone("3001234567", "Claro", person)));

		PersonaDocument persona = personaMapperMongo.fromDomainToAdapter(person);

		assertEquals("M", persona.getGenero());
		EstudiosDocument estudio = persona.getEstudios().get(0);
		assertEquals("123-1", estudio.getId());
		assertNull(estudio.getPrimaryPersona().getEstudios());
		assertNull(estudio.getPrimaryProfesion().getEstudios());
		TelefonoDocument telefono = persona.getTelefonos().get(0);
		assertEquals(123, telefono.getPrimaryDuenio().getId());
		assertNull(telefono.getPrimaryDuenio().getTelefonos());
	}

	@Test
	void fromDomainToAdapterRejectsGenderOther() {
		Person person = new Person(123, "Alex", "Diaz", Gender.OTHER);

		assertThrows(IllegalArgumentException.class, () -> personaMapperMongo.fromDomainToAdapter(person));
	}

	@Test
	void fromDomainToAdapterRejectsNullGender() {
		Person person = new Person();
		person.setIdentification(123);

		assertThrows(IllegalArgumentException.class, () -> personaMapperMongo.fromDomainToAdapterShallow(person));
	}
}
