package co.edu.javeriana.as.personapp.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

	@Mock
	private PersonOutputPort port;

	@Test
	void createRejectsExistingPerson() {
		PersonUseCase useCase = new PersonUseCase(port);
		Person existente = new Person(123, "Pepe", "Perez", Gender.MALE);
		when(port.findById(123)).thenReturn(existente);

		assertThatThrownBy(() -> useCase.create(new Person(123, "Otro", "Otro", Gender.FEMALE)))
				.isInstanceOf(AlreadyExistException.class);
		verify(port, never()).save(any());
	}

	@Test
	void createSavesNewPerson() throws Exception {
		PersonUseCase useCase = new PersonUseCase(port);
		Person nueva = new Person(555, "Ana", "Lopez", Gender.FEMALE);
		when(port.findById(555)).thenReturn(null);
		when(port.save(nueva)).thenReturn(nueva);

		assertThat(useCase.create(nueva)).isSameAs(nueva);
	}

	@Test
	void editRejectsMissingPerson() {
		PersonUseCase useCase = new PersonUseCase(port);
		when(port.findById(1)).thenReturn(null);

		assertThatThrownBy(() -> useCase.edit(1, new Person(1, "A", "B", Gender.OTHER)))
				.isInstanceOf(NoExistException.class);
	}

	@Test
	void dropRejectsMissingPerson() {
		PersonUseCase useCase = new PersonUseCase(port);
		when(port.findById(1)).thenReturn(null);

		assertThatThrownBy(() -> useCase.drop(1)).isInstanceOf(NoExistException.class);
	}

	@Test
	void getPhonesReturnsPhonesOfExistingPerson() throws Exception {
		PersonUseCase useCase = new PersonUseCase(port);
		Person persona = new Person(9, "Fede", "Perez", Gender.MALE);
		persona.setPhoneNumbers(new ArrayList<>());
		when(port.findById(9)).thenReturn(persona);

		assertThat(useCase.getPhones(9)).isEmpty();
	}

}
