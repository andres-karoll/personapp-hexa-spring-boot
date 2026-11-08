package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.util.Consola;

public class ProfesionMenu extends MenuBase {

	private final ProfesionInputAdapterCli adapter;

	public ProfesionMenu(ProfesionInputAdapterCli adapter, Scanner keyboard) {
		super(keyboard, "Profesiones");
		this.adapter = adapter;
	}

	@Override
	protected void setDatabase(String database) throws InvalidOptionException {
		adapter.setDatabase(database);
	}

	@Override
	protected void ejecutar(int opcion) throws NoExistException, AlreadyExistException {
		switch (opcion) {
		case VER_TODOS:

			adapter.historial().forEach(System.out::println);
			break;
		case BUSCAR:
			System.out.println(adapter.buscar(leerId()));
			break;
		case CREAR: {
			Integer id = leerId();
			String nombre = Consola.leerTexto(keyboard, "Nombre: ");
			String descripcion = Consola.leerOpcional(keyboard, "Descripción (vacío para omitir): ");
			System.out.println("Creada: " + adapter.crear(id, nombre, descripcion));
			break;
		}
		case EDITAR: {
			Integer id = leerId();
			String nombre = Consola.leerTexto(keyboard, "Nombre: ");
			String descripcion = Consola.leerOpcional(keyboard, "Descripción (vacío para omitir): ");
			System.out.println("Actualizada: " + adapter.editar(id, nombre, descripcion));
			break;
		}
		case ELIMINAR:
			System.out.println(adapter.eliminar(leerId()) ? "Profesión eliminada." : "No se pudo eliminar.");
			break;
		default:
			System.out.println("La opción elegida no es válida.");
		}
	}

	private Integer leerId() {
		return Consola.leerEntero(keyboard, "Id de la profesión: ");
	}
}
