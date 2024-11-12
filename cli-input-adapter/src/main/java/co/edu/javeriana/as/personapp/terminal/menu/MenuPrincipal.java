package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuPrincipal {
	
	//Beans
	@Autowired
	private PersonaInputAdapterCli personaInputAdapterCli;
	@Autowired
	private TelefonoInputAdapterCli telefonoInputAdapterCli;

	@Autowired
	private ProfesionInputAdapterCli profesionInputAdapterCli;

	@Autowired
	private EstudioInputAdapterCli estudioInputAdapterCli;

	private static final int SALIR = 0;
	private static final int MODULO_PERSONA = 1;
	private static final int MODULO_PROFESION = 2;
	private static final int MODULO_TELEFONO = 3;
	private static final int MODULO_ESTUDIO = 4;

	//Menus
	private final PersonaMenu personaMenu;

	private final TelefonoMenu telefonoMenu;

	private final ProfesionMenu profesionMenu;

	private final EstudioMenu estudioMenu;

	private final Scanner keyboard;

    public MenuPrincipal() {
        this.personaMenu = new PersonaMenu();
		this.telefonoMenu = new TelefonoMenu();
		this.profesionMenu = new ProfesionMenu();
		this.estudioMenu = new EstudioMenu();

        this.keyboard = new Scanner(System.in);
    }

	public void inicio() {
		
		//personaMenu = new PersonaMenu(personaInputAdapterCli);
		boolean isValid = false;
		do {
			mostrarMenu();
			int opcion = leerOpcion();
			switch (opcion) {
			case SALIR:
				isValid = true;
				break;
			case MODULO_PERSONA:
				personaMenu.iniciarMenu(personaInputAdapterCli, keyboard);
				log.info("volvio");
				break;
			case MODULO_PROFESION:
				profesionMenu.iniciarMenu(profesionInputAdapterCli, keyboard);
				log.info("volvio");

				break;
			case MODULO_TELEFONO:
				telefonoMenu.iniciarMenu(telefonoInputAdapterCli, keyboard);
				log.info("volvio");
				break;
			case MODULO_ESTUDIO:
				estudioMenu.iniciarMenu(estudioInputAdapterCli, keyboard);
				log.warn("Implementar Menu");
				break;
			default:
				log.warn("La opción elegida no es válida.");
			}

		} while (!isValid);
		keyboard.close();
	}

	private void mostrarMenu() {
		System.out.println("----------------------");
		System.out.println(MODULO_PERSONA + " para trabajar con el Modulo de Personas");
		System.out.println(MODULO_PROFESION + " para trabajar con el Modulo de Profesiones");
		System.out.println(MODULO_TELEFONO + " para trabajar con el Modulo de Telefonos");
		System.out.println(MODULO_ESTUDIO + " para trabajar con el Modulo de Estudios");
		System.out.println(SALIR + " para Salir");
	}

	private int leerOpcion() {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			return leerOpcion();
		}
	}

}
