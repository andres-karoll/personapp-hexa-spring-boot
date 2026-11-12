package co.edu.javeriana.as.personapp.mongo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

class EstudiosMapperMongoTest {

	private EstudiosMapperMongo estudiosMapperMongo;

	private PersonaMapperMongo personaMapperMongo;

	private ProfesionMapperMongo profesionMapperMongo;

	@BeforeEach
	void setUp() {
		estudiosMapperMongo = new EstudiosMapperMongo();
		personaMapperMongo = spy(new PersonaMapperMongo());
		profesionMapperMongo = spy(new ProfesionMapperMongo());
		ReflectionTestUtils.setField(estudiosMapperMongo, "personaMapperMongo", personaMapperMongo);
		ReflectionTestUtils.setField(estudiosMapperMongo, "profesionMapperMongo", profesionMapperMongo);
	}

	@Test
	void fromAdapterToDomainReturnsStudyUsingShallowMappers() {
		PersonaDocument persona = new PersonaDocument(123, "Ana", "Perez", "F", 30, null, null);
		ProfesionDocument profesion = new ProfesionDocument(1, "Ingeniero", "Sistemas", null);
		EstudiosDocument estudio = new EstudiosDocument("123-1", persona, profesion, LocalDate.of(2020, 1, 15),
				"Javeriana");

		Study study = estudiosMapperMongo.fromAdapterToDomain(estudio);

		assertNotNull(study);
		assertEquals(123, study.getPerson().getIdentification());
		assertEquals(1, study.getProfession().getIdentification());
		assertEquals("Javeriana", study.getUniversityName());
		verify(personaMapperMongo).fromAdapterToDomainShallow(persona);
		verify(personaMapperMongo, never()).fromAdapterToDomain(any());
		verify(profesionMapperMongo).fromAdapterToDomainShallow(profesion);
		verify(profesionMapperMongo, never()).fromAdapterToDomain(any());
	}
}
