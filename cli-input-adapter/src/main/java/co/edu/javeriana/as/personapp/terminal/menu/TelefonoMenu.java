package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.util.Consola;

public class TelefonoMenu extends MenuBase {

	private final TelefonoInputAdapterCli adapter;

	public TelefonoMenu(TelefonoInputAdapterCli adapter, Scanner keyboard) {
		super(keyboard, "Telefonos");
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
			System.out.println(adapter.buscar(leerNumero()));
			break;
		case CREAR: {
			String numero = leerNumero();
			String operador = Consola.leerTexto(keyboard, "Operador: ");
			Integer duenio = Consola.leerEntero(keyboard, "Cédula del dueño: ");
			System.out.println("Creado: " + adapter.crear(numero, operador, duenio));
			break;
		}
		case EDITAR: {
			String numero = leerNumero();
			String operador = Consola.leerTexto(keyboard, "Operador: ");
			Integer duenio = Consola.leerEntero(keyboard, "Cédula del dueño: ");
			System.out.println("Actualizado: " + adapter.editar(numero, operador, duenio));
			break;
		}
		case ELIMINAR:
			System.out.println(adapter.eliminar(leerNumero()) ? "Teléfono eliminado." : "No se pudo eliminar.");
			break;
		default:
			System.out.println("La opción elegida no es válida.");
		}
	}

	private String leerNumero() {
		return Consola.leerTexto(keyboard, "Número de teléfono: ");
	}
}
