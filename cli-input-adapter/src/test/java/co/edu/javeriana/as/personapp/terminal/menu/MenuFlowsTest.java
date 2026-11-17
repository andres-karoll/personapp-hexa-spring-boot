package co.edu.javeriana.as.personapp.terminal.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;

/** Flujos de los menús de consola con entrada simulada y adaptadores simulados. */
class MenuFlowsTest {

	private final PrintStream originalOut = System.out;
	private final ByteArrayOutputStream salida = new ByteArrayOutputStream();

	@BeforeEach
	void capturarSalida() {
		System.setOut(new PrintStream(salida, true));
	}

	@AfterEach
	void restaurarSalida() {
		System.setOut(originalOut);
	}

	private static Scanner teclado(String... lineas) {
		return new Scanner(String.join("\n", lineas) + "\n");
	}

	@Test
	void personaCrearPreguntaDeNuevoElGeneroHastaRecibirMOF() throws Exception {
		PersonaInputAdapterCli adapter = mock(PersonaInputAdapterCli.class);
		when(adapter.crear(eq(5), eq("Ana"), eq("Lopez"), eq(Gender.FEMALE), isNull()))
				.thenReturn(new Person(5, "Ana", "Lopez", Gender.FEMALE));
		Scanner in = teclado("1", "3", "5", "Ana", "Lopez", "X", "F", "", "0", "0");

		new PersonaMenu(adapter, in).iniciarMenu();

		verify(adapter).setDatabase("MARIA");
		verify(adapter).crear(5, "Ana", "Lopez", Gender.FEMALE, null);
		assertThat(salida.toString()).contains("Ingrese M o F.");
	}

	@Test
	void personaListarMuestraLosRegistros() throws Exception {
		PersonaInputAdapterCli adapter = mock(PersonaInputAdapterCli.class);
		when(adapter.historial()).thenReturn(java.util.List.of(new Person(1, "Pepe", "Perez", Gender.MALE)));
		Scanner in = teclado("2", "1", "0", "0");

		new PersonaMenu(adapter, in).iniciarMenu();

		verify(adapter).setDatabase("MONGO");
		verify(adapter).historial();
		assertThat(salida.toString()).contains("Pepe");
	}

	@Test
	void opcionInvalidaYTextoEnLugarDeNumeroNoRompenElMenu() throws Exception {
		PersonaInputAdapterCli adapter = mock(PersonaInputAdapterCli.class);
		Scanner in = teclado("9", "abc", "1", "0", "0");

		new PersonaMenu(adapter, in).iniciarMenu();

		verify(adapter).setDatabase("MARIA");
		assertThat(salida.toString()).contains("Ingrese un número válido.");
		assertThat(salida.toString()).contains("La opción elegida no es válida.");
	}

	@Test
	void buscarInexistenteMuestraErrorSinCerrarLaSesion() throws Exception {
		PersonaInputAdapterCli adapter = mock(PersonaInputAdapterCli.class);
		when(adapter.buscar(42)).thenThrow(new NoExistException("no existe"));
		Scanner in = teclado("1", "2", "42", "0", "0");

		new PersonaMenu(adapter, in).iniciarMenu();

		assertThat(salida.toString()).contains("Error: no existe");
	}

	@Test
	void profesionCrearConDescripcionOpcionalVacia() throws Exception {
		ProfesionInputAdapterCli adapter = mock(ProfesionInputAdapterCli.class);
		when(adapter.crear(eq(10), eq("Arquitecto"), isNull())).thenReturn(new Profession(10, "Arquitecto"));
		Scanner in = teclado("2", "3", "10", "Arquitecto", "", "0", "0");

		new ProfesionMenu(adapter, in).iniciarMenu();

		verify(adapter).setDatabase("MONGO");
		verify(adapter).crear(10, "Arquitecto", null);
	}

	@Test
	void profesionEliminar() throws Exception {
		ProfesionInputAdapterCli adapter = mock(ProfesionInputAdapterCli.class);
		when(adapter.eliminar(7)).thenReturn(true);
		Scanner in = teclado("1", "5", "7", "0", "0");

		new ProfesionMenu(adapter, in).iniciarMenu();

		verify(adapter).eliminar(7);
		assertThat(salida.toString()).contains("Profesión eliminada.");
	}

	@Test
	void telefonoCrearPasaNumeroOperadorYDueno() throws Exception {
		TelefonoInputAdapterCli adapter = mock(TelefonoInputAdapterCli.class);
		Scanner in = teclado("1", "3", "3001112233", "Claro", "123", "0", "0");

		new TelefonoMenu(adapter, in).iniciarMenu();

		verify(adapter).crear("3001112233", "Claro", 123);
	}

	@Test
	void menuDeBaseDesconocidaNoLlamaAlAdaptador() throws Exception {
		PersonaInputAdapterCli adapter = mock(PersonaInputAdapterCli.class);
		Scanner in = teclado("7", "0");

		new PersonaMenu(adapter, in).iniciarMenu();

		verify(adapter, never()).setDatabase(anyString());
	}

	private static String anyString() {
		return org.mockito.ArgumentMatchers.anyString();
	}
}
