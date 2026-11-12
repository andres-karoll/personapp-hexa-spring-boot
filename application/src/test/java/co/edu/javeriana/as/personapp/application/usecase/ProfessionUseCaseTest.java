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

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@ExtendWith(MockitoExtension.class)
class ProfessionUseCaseTest {

	@Mock
	private ProfessionOutputPort professionPersintence;

	private ProfessionUseCase professionUseCase;

	private Profession profession;

	@BeforeEach
	void setUp() {
		professionUseCase = new ProfessionUseCase(professionPersintence);
		profession = new Profession(1, "Ingeniero");
	}

	@Test
	void createSavesProfession() {
		when(professionPersintence.save(profession)).thenReturn(profession);

		assertSame(profession, professionUseCase.create(profession));
	}

	@Test
	void editAssignsIdentificationAndSaves() throws NoExistException {
		Profession changes = new Profession(99, "Ingeniero de Sistemas");
		when(professionPersintence.findById(1)).thenReturn(profession);
		when(professionPersintence.save(changes)).thenReturn(changes);

		Profession edited = professionUseCase.edit(1, changes);

		assertEquals(1, edited.getIdentification());
		verify(professionPersintence).save(changes);
	}

	@Test
	void editThrowsWhenProfessionDoesNotExist() {
		when(professionPersintence.findById(1)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.edit(1, profession));
		verify(professionPersintence, never()).save(any());
	}

	@Test
	void dropDeletesExistingProfession() throws NoExistException {
		when(professionPersintence.findById(1)).thenReturn(profession);
		when(professionPersintence.delete(1)).thenReturn(true);

		assertTrue(professionUseCase.drop(1));
	}

	@Test
	void dropThrowsWhenProfessionDoesNotExist() {
		when(professionPersintence.findById(1)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.drop(1));
		verify(professionPersintence, never()).delete(any());
	}

	@Test
	void findAllAndCountUseOutputPort() {
		when(professionPersintence.find()).thenReturn(List.of(profession));

		assertEquals(List.of(profession), professionUseCase.findAll());
		assertEquals(1, professionUseCase.count());
	}

	@Test
	void findOneReturnsExistingProfession() throws NoExistException {
		when(professionPersintence.findById(1)).thenReturn(profession);

		assertSame(profession, professionUseCase.findOne(1));
	}

	@Test
	void findOneThrowsWhenProfessionDoesNotExist() {
		when(professionPersintence.findById(1)).thenReturn(null);

		assertThrows(NoExistException.class, () -> professionUseCase.findOne(1));
	}
}
