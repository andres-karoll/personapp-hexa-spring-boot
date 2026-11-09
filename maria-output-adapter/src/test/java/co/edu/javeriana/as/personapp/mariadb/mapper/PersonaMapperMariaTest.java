package co.edu.javeriana.as.personapp.mariadb.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

class PersonaMapperMariaTest {

	private PersonaMapperMaria personaMapperMaria;

	@BeforeEach
	void setUp() {
		personaMapperMaria = new PersonaMapperMaria();
		EstudiosMapperMaria estudiosMapperMaria = new EstudiosMapperMaria();
		TelefonoMapperMaria telefonoMapperMaria = new TelefonoMapperMaria();
		ProfesionMapperMaria profesionMapperMaria = new ProfesionMapperMaria();
		ReflectionTestUtils.setField(personaMapperMaria, "estudiosMapperMaria", estudiosMapperMaria);
		ReflectionTestUtils.setField(personaMapperMaria, "telefonoMapperMaria", telefonoMapperMaria);
		ReflectionTestUtils.setField(estudiosMapperMaria, "personaMapperMaria", personaMapperMaria);
		ReflectionTestUtils.setField(estudiosMapperMaria, "profesionMapperMaria", profesionMapperMaria);
		ReflectionTestUtils.setField(telefonoMapperMaria, "personaMapperMaria", personaMapperMaria);
		ReflectionTestUtils.setField(profesionMapperMaria, "estudiosMapperMaria", estudiosMapperMaria);
	}

	@Test
	void fromAdapterToDomainWithStudiesAndPhonesDoesNotRecurse() {
		PersonaEntity persona = new PersonaEntity(123, "Ana", "Perez", 'F');
		ProfesionEntity profesion = new ProfesionEntity(1, "Ingeniero");
		EstudiosEntity estudio = new EstudiosEntity(1, 123);
		estudio.setPersona(persona);
		estudio.setProfesion(profesion);
		estudio.setUniver("Javeriana");
		profesion.setEstudios(List.of(estudio));
		TelefonoEntity telefono = new TelefonoEntity("3001234567", "Claro");
		telefono.setDuenio(persona);
		persona.setEstudios(List.of(estudio));
		persona.setTelefonos(List.of(telefono));

		Person person = personaMapperMaria.fromAdapterToDomain(persona);

		assertEquals(Gender.FEMALE, person.getGender());
		Study study = person.getStudies().get(0);
		assertEquals(123, study.getPerson().getIdentification());
		assertEquals(1, study.getProfession().getIdentification());
		assertEquals("Javeriana", study.getUniversityName());
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

		PersonaEntity persona = personaMapperMaria.fromDomainToAdapter(person);

		assertEquals('M', persona.getGenero());
		assertEquals(123, persona.getEstudios().get(0).getEstudiosPK().getCcPer());
		TelefonoEntity telefono = persona.getTelefonos().get(0);
		assertEquals(123, telefono.getDuenio().getCc());
		assertNull(telefono.getDuenio().getTelefonos());
	}

	@Test
	void fromDomainToAdapterMapsAgeNullAsNull() {
		Person person = new Person(123, "Luis", "Gomez", Gender.MALE);

		assertNull(personaMapperMaria.fromDomainToAdapter(person).getEdad());
	}

	@Test
	void fromDomainToAdapterRejectsGenderOther() {
		Person person = new Person(123, "Alex", "Diaz", Gender.OTHER);

		assertThrows(IllegalArgumentException.class, () -> personaMapperMaria.fromDomainToAdapter(person));
	}

	@Test
	void fromDomainToAdapterRejectsNullGender() {
		Person person = new Person();
		person.setIdentification(123);

		assertThrows(IllegalArgumentException.class, () -> personaMapperMaria.fromDomainToAdapterShallow(person));
	}
}
