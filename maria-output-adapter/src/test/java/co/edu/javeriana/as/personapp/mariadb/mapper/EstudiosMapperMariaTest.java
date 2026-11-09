package co.edu.javeriana.as.personapp.mariadb.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

class EstudiosMapperMariaTest {

	private EstudiosMapperMaria estudiosMapperMaria;

	private PersonaMapperMaria personaMapperMaria;

	private ProfesionMapperMaria profesionMapperMaria;

	@BeforeEach
	void setUp() {
		estudiosMapperMaria = new EstudiosMapperMaria();
		personaMapperMaria = spy(new PersonaMapperMaria());
		profesionMapperMaria = spy(new ProfesionMapperMaria());
		ReflectionTestUtils.setField(estudiosMapperMaria, "personaMapperMaria", personaMapperMaria);
		ReflectionTestUtils.setField(estudiosMapperMaria, "profesionMapperMaria", profesionMapperMaria);
	}

	@Test
	void fromAdapterToDomainReturnsStudyUsingShallowMappers() {
		LocalDate graduation = LocalDate.of(2020, 1, 15);
		EstudiosEntity estudio = new EstudiosEntity(1, 123);
		estudio.setPersona(new PersonaEntity(123, "Ana", "Perez", 'F'));
		estudio.setProfesion(new ProfesionEntity(1, "Ingeniero"));
		estudio.setFecha(Date.from(graduation.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		estudio.setUniver("Javeriana");

		Study study = estudiosMapperMaria.fromAdapterToDomain(estudio);

		assertNotNull(study);
		assertEquals(123, study.getPerson().getIdentification());
		assertEquals(1, study.getProfession().getIdentification());
		assertEquals(graduation, study.getGraduationDate());
		assertEquals("Javeriana", study.getUniversityName());
		verify(personaMapperMaria).fromAdapterToDomainShallow(estudio.getPersona());
		verify(personaMapperMaria, never()).fromAdapterToDomain(any());
		verify(profesionMapperMaria).fromAdapterToDomainShallow(estudio.getProfesion());
		verify(profesionMapperMaria, never()).fromAdapterToDomain(any());
	}
}
