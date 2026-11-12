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

    @Test
    void sqlDateAndMissingRelationsRetainCompositeIdentifiers() {
        EstudiosEntity entity = new EstudiosEntity(7, 123);
        entity.setFecha(java.sql.Date.valueOf("2024-02-29"));
        Study result = estudiosMapperMaria.fromAdapterToDomain(entity);
        assertEquals(LocalDate.of(2024, 2, 29), result.getGraduationDate());
        assertEquals(123, result.getPerson().getIdentification());
        assertEquals(7, result.getProfession().getIdentification());
    }

    @Test
    void domainMappingSetsBothParentsWithoutRecursion() {
        co.edu.javeriana.as.personapp.domain.Person person = new co.edu.javeriana.as.personapp.domain.Person(
            123, "Ana", "Perez", co.edu.javeriana.as.personapp.domain.Gender.FEMALE);
        co.edu.javeriana.as.personapp.domain.Profession profession = new co.edu.javeriana.as.personapp.domain.Profession(7, "Ingenieria");
        Study study = new Study(person, profession);
        person.setStudies(java.util.List.of(study));
        profession.setStudies(java.util.List.of(study));
        EstudiosEntity entity = estudiosMapperMaria.fromDomainToAdapter(study);
        assertEquals(123, entity.getPersona().getCc());
        assertEquals(7, entity.getProfesion().getId());
        assertEquals(new co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK(7, 123), entity.getEstudiosPK());
    }
}
