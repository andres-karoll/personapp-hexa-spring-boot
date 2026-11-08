package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.util.Consola;

public class PersonaMenu extends MenuBase {

	private final PersonaInputAdapterCli adapter;
	private final PersonaMapperCli mapper = new PersonaMapperCli();

	public PersonaMenu(PersonaInputAdapterCli adapter, Scanner keyboard) {
		super(keyboard, "Personas");
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
			adapter.historial().forEach(p -> System.out.println(mapper.fromDomainToAdapterCli(p)));
			break;
		case BUSCAR:
			System.out.println(mapper.fromDomainToAdapterCli(adapter.buscar(leerId())));
			break;
		case CREAR: {
			Integer id = leerId();
			String nombre = Consola.leerTexto(keyboard, "Nombre: ");
			String apellido = Consola.leerTexto(keyboard, "Apellido: ");
			Gender genero = leerGenero();
			Integer edad = leerEdad();
			System.out.println("Creada: " + mapper.fromDomainToAdapterCli(adapter.crear(id, nombre, apellido, genero, edad)));
			break;
		}
		case EDITAR: {
			Integer id = leerId();
			String nombre = Consola.leerTexto(keyboard, "Nombre: ");
			String apellido = Consola.leerTexto(keyboard, "Apellido: ");
			Gender genero = leerGenero();
			Integer edad = leerEdad();
			System.out.println("Actualizada: " + mapper.fromDomainToAdapterCli(adapter.editar(id, nombre, apellido, genero, edad)));
			break;
		}
		case ELIMINAR:
			System.out.println(adapter.eliminar(leerId()) ? "Persona eliminada." : "No se pudo eliminar.");
			break;
		default:
			System.out.println("La opción elegida no es válida.");
		}
	}

	private Integer leerId() {
		return Consola.leerEntero(keyboard, "Cédula: ");
	}

	private Gender leerGenero() {
		String g = Consola.leerTexto(keyboard, "Género (M/F/O): ").toUpperCase();
		return g.equals("M") ? Gender.MALE : g.equals("F") ? Gender.FEMALE : Gender.OTHER;
	}

	private Integer leerEdad() {
		String e = Consola.leerOpcional(keyboard, "Edad (vacío para omitir): ");
		return e == null ? null : Integer.valueOf(e);
	}
}
