package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@ExtendWith(MockitoExtension.class)
class StudyUseCaseTest {

	private static final Integer PROFESSION_ID = 1;
	private static final Integer PERSON_ID = 123;

	@Mock
	private StudyOutputPort studyPersintence;

	private StudyUseCase studyUseCase;

	private Study study;

	@BeforeEach
	void setUp() {
		studyUseCase = new StudyUseCase(studyPersintence);
		study = new Study(new Person(PERSON_ID, "Ana", "Perez", Gender.FEMALE),
				new Profession(PROFESSION_ID, "Ingeniero"));
	}

	@Test
	void createSavesStudy() {
		when(studyPersintence.save(study)).thenReturn(study);

		assertSame(study, studyUseCase.create(study));
	}

	@Test
	void editKeepsCompositeKeyAndSaves() throws NoExistException {
		Study changes = new Study(new Person(999, "Luis", "Gomez", Gender.MALE), new Profession(99, "Medico"));
		changes.setUniversityName("Javeriana");
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(study);
		when(studyPersintence.save(changes)).thenReturn(changes);

		Study edited = studyUseCase.edit(PROFESSION_ID, PERSON_ID, changes);

		assertEquals(PROFESSION_ID, edited.getProfession().getIdentification());
		assertEquals(PERSON_ID, edited.getPerson().getIdentification());
		assertEquals("Javeriana", edited.getUniversityName());
		verify(studyPersintence).save(changes);
	}

	@Test
	void editThrowsWhenStudyDoesNotExist() {
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.edit(PROFESSION_ID, PERSON_ID, study));
		verify(studyPersintence, never()).save(any());
	}

	@Test
	void dropDeletesExistingStudy() throws NoExistException {
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(study);
		when(studyPersintence.delete(PROFESSION_ID, PERSON_ID)).thenReturn(true);

		assertTrue(studyUseCase.drop(PROFESSION_ID, PERSON_ID));
	}

	@Test
	void dropThrowsWhenStudyDoesNotExist() {
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.drop(PROFESSION_ID, PERSON_ID));
		verify(studyPersintence, never()).delete(anyInt(), anyInt());
	}

	@Test
	void findAllAndCountUseOutputPort() {
		when(studyPersintence.find()).thenReturn(List.of(study));

		assertEquals(List.of(study), studyUseCase.findAll());
		assertEquals(1, studyUseCase.count());
	}

	@Test
	void findOneReturnsExistingStudy() throws NoExistException {
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(study);

		assertSame(study, studyUseCase.findOne(PROFESSION_ID, PERSON_ID));
	}

	@Test
	void findOneThrowsWhenStudyDoesNotExist() {
		when(studyPersintence.findById(PROFESSION_ID, PERSON_ID)).thenReturn(null);

		assertThrows(NoExistException.class, () -> studyUseCase.findOne(PROFESSION_ID, PERSON_ID));
	}
}
