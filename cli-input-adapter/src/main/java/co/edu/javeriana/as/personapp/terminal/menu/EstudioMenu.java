package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;
import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.util.Consola;

public class EstudioMenu extends MenuBase {

	private final EstudioInputAdapterCli adapter;

	public EstudioMenu(EstudioInputAdapterCli adapter, Scanner keyboard) {
		super(keyboard, "Estudios");
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
			System.out.println(adapter.buscar(leerDni(), leerProfesion()));
			break;
		case CREAR: {
			Integer dni = leerDni();
			Integer profesion = leerProfesion();
			LocalDate fecha = leerFecha();
			String universidad = Consola.leerOpcional(keyboard, "Universidad (vacío para omitir): ");
			System.out.println("Creado: " + adapter.crear(dni, profesion, fecha, universidad));
			break;
		}
		case EDITAR: {
			Integer dni = leerDni();
			Integer profesion = leerProfesion();
			LocalDate fecha = leerFecha();
			String universidad = Consola.leerOpcional(keyboard, "Universidad (vacío para omitir): ");
			System.out.println("Actualizado: " + adapter.editar(dni, profesion, fecha, universidad));
			break;
		}
		case ELIMINAR:
			System.out.println(adapter.eliminar(leerDni(), leerProfesion()) ? "Estudio eliminado." : "No se pudo eliminar.");
			break;
		default:
			System.out.println("La opción elegida no es válida.");
		}
	}

	private Integer leerDni() {
		return Consola.leerEntero(keyboard, "Cédula de la persona: ");
	}

	private Integer leerProfesion() {
		return Consola.leerEntero(keyboard, "Id de la profesión: ");
	}

	private LocalDate leerFecha() {
		String f = Consola.leerOpcional(keyboard, "Fecha de grado yyyy-MM-dd (vacío para omitir): ");
		return f == null ? null : LocalDate.parse(f);
	}
}
