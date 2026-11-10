package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.util.Consola;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuPrincipal {

	@Autowired
	private PersonaInputAdapterCli personaInputAdapterCli;

	@Autowired
	private ProfesionInputAdapterCli profesionInputAdapterCli;

	@Autowired
	private TelefonoInputAdapterCli telefonoInputAdapterCli;

	@Autowired
	private EstudioInputAdapterCli estudioInputAdapterCli;

	private static final int SALIR = 0;
	private static final int MODULO_PERSONA = 1;
	private static final int MODULO_PROFESION = 2;
	private static final int MODULO_TELEFONO = 3;
	private static final int MODULO_ESTUDIO = 4;

	public void inicio() {
		Scanner keyboard = new Scanner(System.in);
		boolean salir = false;
		do {
			mostrarMenu();
			int opcion = Consola.leerEntero(keyboard, "Ingrese una opción: ");
			switch (opcion) {
			case SALIR:
				salir = true;
				break;
			case MODULO_PERSONA:
				new PersonaMenu(personaInputAdapterCli, keyboard).iniciarMenu();
				break;
			case MODULO_PROFESION:
				new ProfesionMenu(profesionInputAdapterCli, keyboard).iniciarMenu();
				break;
			case MODULO_TELEFONO:
				new TelefonoMenu(telefonoInputAdapterCli, keyboard).iniciarMenu();
				break;
			case MODULO_ESTUDIO:
				new EstudioMenu(estudioInputAdapterCli, keyboard).iniciarMenu();
				break;
			default:
				log.warn("La opción elegida no es válida.");
			}
		} while (!salir);
	}

	private void mostrarMenu() {
		System.out.println("----------------------");
		System.out.println(MODULO_PERSONA + " para trabajar con el Modulo de Personas");
		System.out.println(MODULO_PROFESION + " para trabajar con el Modulo de Profesiones");
		System.out.println(MODULO_TELEFONO + " para trabajar con el Modulo de Telefonos");
		System.out.println(MODULO_ESTUDIO + " para trabajar con el Modulo de Estudios");
		System.out.println(SALIR + " para Salir");
	}
}
