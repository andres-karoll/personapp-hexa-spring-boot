package co.edu.javeriana.as.personapp.application.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;

@ExtendWith(MockitoExtension.class)
class PhoneUseCaseTest {

	@Mock
	private PhoneOutputPort port;

	@Test
	void createRejectsExistingNumber() {
		PhoneUseCase useCase = new PhoneUseCase(port);
		Person dueno = new Person();
		dueno.setIdentification(1);
		Phone telefono = new Phone("3001112233", "Claro", dueno);
		when(port.findById("3001112233")).thenReturn(telefono);

		assertThatThrownBy(() -> useCase.create(telefono)).isInstanceOf(AlreadyExistException.class);
	}

	@Test
	void dropRejectsMissingNumber() {
		PhoneUseCase useCase = new PhoneUseCase(port);
		when(port.findById("0000")).thenReturn(null);

		assertThatThrownBy(() -> useCase.drop("0000")).isInstanceOf(NoExistException.class);
	}
}
