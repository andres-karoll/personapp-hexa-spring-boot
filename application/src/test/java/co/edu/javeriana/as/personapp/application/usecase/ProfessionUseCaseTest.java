package co.edu.javeriana.as.personapp.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@ExtendWith(MockitoExtension.class)
class ProfessionUseCaseTest {

	@Mock
	private ProfessionOutputPort port;

	private ProfessionUseCase useCase;

	@Test
	void createSavesWhenIdIsFree() throws Exception {
		useCase = new ProfessionUseCase(port);
		Profession nueva = new Profession(10, "Arquitecto");
		when(port.findById(10)).thenReturn(null);
		when(port.save(nueva)).thenReturn(nueva);

		assertThat(useCase.create(nueva)).isSameAs(nueva);
		verify(port).save(nueva);
	}

	@Test
	void createRejectsExistingId() {
		useCase = new ProfessionUseCase(port);
		when(port.findById(1)).thenReturn(new Profession(1, "Ingeniero"));

		assertThatThrownBy(() -> useCase.create(new Profession(1, "Dup")))
				.isInstanceOf(AlreadyExistException.class);
		verify(port, never()).save(any());
	}

	@Test
	void editRejectsMissingProfession() {
		useCase = new ProfessionUseCase(port);
		when(port.findById(99)).thenReturn(null);

		assertThatThrownBy(() -> useCase.edit(99, new Profession(99, "X")))
				.isInstanceOf(NoExistException.class);
	}

	@Test
	void editKeepsIdentificationFromPathAndSaves() throws Exception {
		useCase = new ProfessionUseCase(port);
		when(port.findById(2)).thenReturn(new Profession(2, "Medico"));
		when(port.save(any())).thenAnswer(inv -> inv.getArgument(0));

		Profession editada = useCase.edit(2, new Profession(2, "Medico General"));

		assertThat(editada.getIdentification()).isEqualTo(2);
		assertThat(editada.getName()).isEqualTo("Medico General");
	}

	@Test
	void dropDeletesOnlyExistingProfession() throws Exception {
		useCase = new ProfessionUseCase(port);
		when(port.findById(3)).thenReturn(new Profession(3, "Abogado"));
		when(port.delete(3)).thenReturn(true);

		assertThat(useCase.drop(3)).isTrue();
	}

	@Test
	void findOneThrowsWhenMissing() {
		useCase = new ProfessionUseCase(port);
		when(port.findById(5)).thenReturn(null);

		assertThatThrownBy(() -> useCase.findOne(5)).isInstanceOf(NoExistException.class);
	}

	@Test
	void countUsesFindAll() {
		useCase = new ProfessionUseCase(port);
		when(port.find()).thenReturn(List.of(new Profession(1, "A"), new Profession(2, "B")));

		assertThat(useCase.count()).isEqualTo(2);
	}

}
