package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

	@Mock
	private PersonOutputPort personPersintence;

	private PersonUseCase personUseCase;

	private Person person;

	@BeforeEach
	void setUp() {
		personUseCase = new PersonUseCase(personPersintence);
		person = new Person(123, "Ana", "Perez", Gender.FEMALE);
	}

	@Test
	void createSavesPerson() {
		when(personPersintence.save(person)).thenReturn(person);

		assertSame(person, personUseCase.create(person));
	}

	@Test
	void editAssignsIdentificationAndSaves() throws NoExistException {
		Person changes = new Person(999, "Ana Maria", "Perez", Gender.FEMALE);
		when(personPersintence.findById(123)).thenReturn(person);
		when(personPersintence.save(changes)).thenReturn(changes);

		Person edited = personUseCase.edit(123, changes);

		assertEquals(123, edited.getIdentification());
		verify(personPersintence).save(changes);
	}

	@Test
	void editThrowsWhenPersonDoesNotExist() {
		when(personPersintence.findById(123)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.edit(123, person));
		verify(personPersintence, never()).save(any());
	}

	@Test
	void dropDeletesExistingPerson() throws NoExistException {
		when(personPersintence.findById(123)).thenReturn(person);
		when(personPersintence.delete(123)).thenReturn(true);

		assertTrue(personUseCase.drop(123));
	}

	@Test
	void dropThrowsWhenPersonDoesNotExist() {
		when(personPersintence.findById(123)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.drop(123));
		verify(personPersintence, never()).delete(any());
	}

	@Test
	void findAllAndCountUseOutputPort() {
		when(personPersintence.find()).thenReturn(List.of(person));

		assertEquals(List.of(person), personUseCase.findAll());
		assertEquals(1, personUseCase.count());
	}

	@Test
	void findOneReturnsExistingPerson() throws NoExistException {
		when(personPersintence.findById(123)).thenReturn(person);

		assertSame(person, personUseCase.findOne(123));
	}

	@Test
	void findOneThrowsWhenPersonDoesNotExist() {
		when(personPersintence.findById(123)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.findOne(123));
	}

	@Test
	void getPhonesAndStudiesReturnPersonRelations() throws NoExistException {
		Phone phone = new Phone("3001234567", "Claro", person);
		Study study = new Study(person, new Profession(1, "Ingeniero"));
		person.setPhoneNumbers(List.of(phone));
		person.setStudies(List.of(study));
		when(personPersintence.findById(123)).thenReturn(person);

		assertEquals(List.of(phone), personUseCase.getPhones(123));
		assertEquals(List.of(study), personUseCase.getStudies(123));
	}

	@Test
	void getPhonesAndStudiesThrowWhenPersonDoesNotExist() {
		when(personPersintence.findById(123)).thenReturn(null);

		assertThrows(NoExistException.class, () -> personUseCase.getPhones(123));
		assertThrows(NoExistException.class, () -> personUseCase.getStudies(123));
	}
}
