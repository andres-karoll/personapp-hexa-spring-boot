package co.edu.javeriana.as.personapp.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@ExtendWith(MockitoExtension.class)
class PersonaInputAdapterRestTest {

	@Mock
	private PersonOutputPort personOutputPortMaria;

	@Mock
	private PersonOutputPort personOutputPortMongo;

	private PersonaInputAdapterRest personaInputAdapterRest;

	@BeforeEach
	void setUp() {
		personaInputAdapterRest = new PersonaInputAdapterRest();
		ReflectionTestUtils.setField(personaInputAdapterRest, "personOutputPortMaria", personOutputPortMaria);
		ReflectionTestUtils.setField(personaInputAdapterRest, "personOutputPortMongo", personOutputPortMongo);
		ReflectionTestUtils.setField(personaInputAdapterRest, "personaMapperRest", new PersonaMapperRest());
	}

	@Test
	void crearPersonaRespondsWithChosenDatabase() {
		when(personOutputPortMongo.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

		PersonaResponse response = personaInputAdapterRest
				.crearPersona(new PersonaRequest("123", "Ana", "Perez", "30", "F", "mongo"));

		assertEquals("MongoDB", response.getDatabase());
		assertEquals("OK", response.getStatus());
		verify(personOutputPortMaria, never()).save(any());
	}

	@Test
	void crearPersonaReturnsErrorStatusForInvalidDatabase() {
		PersonaResponse response = personaInputAdapterRest
				.crearPersona(new PersonaRequest("123", "Ana", "Perez", "30", "F", "ORACLE"));

		assertTrue(response.getStatus().startsWith("ERROR"));
		verifyNoInteractions(personOutputPortMaria, personOutputPortMongo);
	}

	@Test
	void crearPersonaReturnsErrorStatusForInvalidData() {
		PersonaResponse response = personaInputAdapterRest
				.crearPersona(new PersonaRequest("123", "Ana", "Perez", "30", "X", "MARIA"));

		assertTrue(response.getStatus().startsWith("ERROR"));
		verifyNoInteractions(personOutputPortMaria, personOutputPortMongo);
	}
}
