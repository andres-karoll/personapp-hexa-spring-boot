package co.edu.javeriana.as.personapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

/** Contrato HTTP de los controladores: códigos de estado y formato de error, con adaptadores simulados. */
class ControllerContractTest {

	@Mock private PersonaInputAdapterRest personaAdapter;
	@Mock private ProfesionInputAdapterRest profesionAdapter;
	@Mock private TelefonoInputAdapterRest telefonoAdapter;
	@Mock private EstudioInputAdapterRest estudioAdapter;
	@InjectMocks private PersonaControllerV1 personaController;
	@InjectMocks private ProfesionControllerV1 profesionController;
	@InjectMocks private TelefonoControllerV1 telefonoController;
	@InjectMocks private EstudioControllerV1 estudioController;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(personaController, profesionController, telefonoController, estudioController)
				.setControllerAdvice(new ApiExceptionHandler()).build();
	}

	@Test
	void personaExistenteRespondeOk() throws Exception {
		when(personaAdapter.buscar(eq("MARIA"), anyInt())).thenReturn(
				new PersonaResponse("123", "Pepe", "Perez", "30", "MALE", "MariaDB", "OK"));
		mvc.perform(get("/api/v1/persona/maria/123"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Pepe"));
	}

	@Test
	void personaInexistenteRespondeNotFoundConFormatoDeError() throws Exception {
		when(personaAdapter.buscar(anyString(), anyInt())).thenThrow(new NoExistException("no existe"));
		mvc.perform(get("/api/v1/persona/maria/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value("ERROR"))
				.andExpect(jsonPath("$.description").value("no existe"));
	}

	@Test
	void personaDuplicadaRespondeConflicto() throws Exception {
		when(personaAdapter.crearPersona(any())).thenThrow(new AlreadyExistException("ya existe"));
		mvc.perform(post("/api/v1/persona").contentType(MediaType.APPLICATION_JSON)
				.content("{\"dni\":\"1\",\"firstName\":\"A\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"M\",\"database\":\"maria\"}"))
				.andExpect(status().isConflict());
	}

	@Test
	void baseInvalidaRespondeBadRequest() throws Exception {
		when(personaAdapter.historial(anyString())).thenThrow(new InvalidOptionException("Invalid database option: x"));
		mvc.perform(get("/api/v1/persona/x"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.description").value("Invalid database option: x"));
	}

	@Test
	void entradaInvalidaRespondeBadRequest() throws Exception {
		when(personaAdapter.crearPersona(any())).thenThrow(new IllegalArgumentException("sex debe ser M o F"));
		mvc.perform(post("/api/v1/persona").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.description").value(org.hamcrest.Matchers.containsString("sex debe ser M o F")));
	}

	@Test
	void jsonMalformadoRespondeBadRequest() throws Exception {
		mvc.perform(post("/api/v1/persona").contentType(MediaType.APPLICATION_JSON).content("{bad"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void eliminarPersonaRespondeNoContent() throws Exception {
		mvc.perform(delete("/api/v1/persona/maria/5")).andExpect(status().isNoContent());
		verify(personaAdapter).eliminar("MARIA", 5);
	}

	@Test
	void profesionCrudUsaElAdaptadorConLaBaseEnMayusculas() throws Exception {
		when(profesionAdapter.buscar(eq("MONGO"), anyInt())).thenReturn(
				new ProfesionResponse("1", "Ing", "d", "MongoDB", "OK"));
		mvc.perform(get("/api/v1/profesion/mongo/1")).andExpect(status().isOk());
		mvc.perform(delete("/api/v1/profesion/mongo/1")).andExpect(status().isNoContent());
		verify(profesionAdapter).eliminar("MONGO", 1);
	}

	@Test
	void telefonoConNumeroComoIdentificador() throws Exception {
		mvc.perform(delete("/api/v1/telefono/maria/3001112233")).andExpect(status().isNoContent());
		verify(telefonoAdapter).eliminar("MARIA", "3001112233");
	}

	@Test
	void estudioConClaveCompuesta() throws Exception {
		mvc.perform(delete("/api/v1/estudio/maria/10/20")).andExpect(status().isNoContent());
		verify(estudioAdapter).eliminar("MARIA", 10, 20);
	}

	@Test
	void estudioNoExistenteRespondeNotFound() throws Exception {
		when(estudioAdapter.buscar(anyString(), anyInt(), anyInt())).thenThrow(new NoExistException("no"));
		mvc.perform(get("/api/v1/estudio/maria/1/1")).andExpect(status().isNotFound());
	}
}
