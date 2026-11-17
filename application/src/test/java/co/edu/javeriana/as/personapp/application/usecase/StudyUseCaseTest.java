package co.edu.javeriana.as.personapp.application.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class StudyUseCaseTest {

	@Mock
	private StudyOutputPort port;

	@Test
	void createUsesBothKeysToCheckDuplicates() {
		StudyUseCase useCase = new StudyUseCase(port);
		Study estudio = studio(1, 2);
		when(port.findById(1, 2)).thenReturn(estudio);

		assertThatThrownBy(() -> useCase.create(estudio)).isInstanceOf(AlreadyExistException.class);
		verify(port, never()).save(estudio);
	}

	@Test
	void editRejectsMissingStudy() {
		StudyUseCase useCase = new StudyUseCase(port);
		when(port.findById(7, 8)).thenReturn(null);

		assertThatThrownBy(() -> useCase.edit(7, 8, studio(7, 8))).isInstanceOf(NoExistException.class);
	}

	@Test
	void dropRejectsMissingStudy() {
		StudyUseCase useCase = new StudyUseCase(port);
		when(port.findById(7, 8)).thenReturn(null);

		assertThatThrownBy(() -> useCase.drop(7, 8)).isInstanceOf(NoExistException.class);
		verify(port, never()).delete(7, 8);
	}

	private static Study studio(int dni, int idProfesion) {
		Person persona = new Person();
		persona.setIdentification(dni);
		return new Study(persona, new Profession(idProfesion, "x"));
	}
}
