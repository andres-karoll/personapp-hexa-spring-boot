package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.util.Consola;

/** Menú común: elegir motor de persistencia y luego ejecutar operaciones CRUD. */
public abstract class MenuBase {

	protected static final int VER_TODOS = 1;
	protected static final int BUSCAR = 2;
	protected static final int CREAR = 3;
	protected static final int EDITAR = 4;
	protected static final int ELIMINAR = 5;

	private static final int REGRESAR = 0;
	private static final int MARIA = 1;
	private static final int MONGO = 2;

	protected final Scanner keyboard;
	private final String modulo;

	protected MenuBase(Scanner keyboard, String modulo) {
		this.keyboard = keyboard;
		this.modulo = modulo;
	}

	protected abstract void setDatabase(String database) throws InvalidOptionException;

	protected abstract void ejecutar(int opcion) throws NoExistException, AlreadyExistException;

	public void iniciarMenu() {
		boolean salir = false;
		do {
			System.out.println("----------------------");
			System.out.println("Modulo de " + modulo + " - motor de persistencia:");
			System.out.println(MARIA + " para MariaDB");
			System.out.println(MONGO + " para MongoDB");
			System.out.println(REGRESAR + " para regresar");
			int opcion = Consola.leerEntero(keyboard, "Ingrese una opción: ");
			switch (opcion) {
			case REGRESAR:
				salir = true;
				break;
			case MARIA:
				seleccionarBase("MARIA");
				break;
			case MONGO:
				seleccionarBase("MONGO");
				break;
			default:
				System.out.println("La opción elegida no es válida.");
			}
		} while (!salir);
	}

	private void seleccionarBase(String database) {
		try {
			setDatabase(database);
			menuOperaciones();
		} catch (InvalidOptionException e) {
			System.out.println(e.getMessage());
		}
	}

	private void menuOperaciones() {
		boolean salir = false;
		do {
			System.out.println("----------------------");
			System.out.println(VER_TODOS + " ver todos");
			System.out.println(BUSCAR + " buscar por identificación");
			System.out.println(CREAR + " crear");
			System.out.println(EDITAR + " editar");
			System.out.println(ELIMINAR + " eliminar");
			System.out.println(REGRESAR + " regresar");
			int opcion = Consola.leerEntero(keyboard, "Ingrese una opción: ");
			if (opcion == REGRESAR) {
				salir = true;
				continue;
			}
			try {
				ejecutar(opcion);
			} catch (NoExistException | AlreadyExistException e) {
				System.out.println("Error: " + e.getMessage());
			} catch (DataIntegrityViolationException | ObjectRetrievalFailureException e) {
				System.out.println("Error: llave duplicada o referencia inexistente (llave foránea).");
			} catch (RuntimeException e) {
				System.out.println("Datos inválidos: " + e.getMessage());
			}
		} while (!salir);
	}
}
