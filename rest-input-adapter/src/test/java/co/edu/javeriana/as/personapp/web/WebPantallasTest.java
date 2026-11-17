package co.edu.javeriana.as.personapp.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

class WebPantallasTest {

	@Mock
	private PersonaInputAdapterRest personas;

	@Mock
	private EstudioInputAdapterRest estudios;

	@InjectMocks
	private PersonaWebController personaController;

	@InjectMocks
	private EstudioWebController estudioController;

	private MockMvc personaMvc;
	private MockMvc estudioMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		personaMvc = MockMvcBuilders.standaloneSetup(personaController).build();
		estudioMvc = MockMvcBuilders.standaloneSetup(estudioController).build();
	}

	@Test
	void listadoDePersonasUsaElMotorElegido() throws Exception {
		when(personas.historial("MONGO")).thenReturn(List.of());

		personaMvc.perform(get("/web/personas").param("db", "MONGO"))
				.andExpect(status().isOk())
				.andExpect(view().name("persona/listado"))
				.andExpect(model().attribute("db", "MONGO"));

		verify(personas).historial("MONGO");
	}

	@Test
	void motorDesconocidoUsaMariaDB() throws Exception {
		when(personas.historial("MARIA")).thenReturn(List.of());

		personaMvc.perform(get("/web/personas").param("db", "OTRO"))
				.andExpect(model().attribute("db", "MARIA"));
	}

	@Test
	void crearPersonaDuplicadaVuelveAlFormularioConMensaje() throws Exception {
		when(personas.crearPersona(any())).thenThrow(new AlreadyExistException("duplicada"));

		personaMvc.perform(post("/web/personas")
						.param("database", "MARIA")
						.param("dni", "123456789")
						.param("firstName", "Pepe")
						.param("lastName", "Perez")
						.param("sex", "M"))
				.andExpect(status().isOk())
				.andExpect(view().name("persona/formulario"))
				.andExpect(model().attribute("error", "Ya existe un registro con esa llave."));
	}

	@Test
	void eliminarPersonaRedirigeAlListadoConMensaje() throws Exception {
		personaMvc.perform(post("/web/personas/MARIA/123456789/eliminar"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/web/personas?db=MARIA"));
	}

	@Test
	void editarEstudioInexistenteRedirigeConError() throws Exception {
		when(estudios.buscar("MARIA", 1, 2)).thenThrow(new NoExistException("no existe"));

		estudioMvc.perform(get("/web/estudios/MARIA/1/2/editar"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void formularioEditarEstudioCargaLosDatos() throws Exception {
		EstudioResponse existente = new EstudioResponse("1", "2", "2015-06-30", "Javeriana", "MARIA", "OK");
		when(estudios.buscar("MARIA", 1, 2)).thenReturn(existente);

		estudioMvc.perform(get("/web/estudios/MARIA/1/2/editar"))
				.andExpect(status().isOk())
				.andExpect(view().name("estudio/formulario"))
				.andExpect(model().attribute("editando", true));
	}
}
