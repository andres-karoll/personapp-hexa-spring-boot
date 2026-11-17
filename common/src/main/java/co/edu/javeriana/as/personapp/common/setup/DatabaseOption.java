package co.edu.javeriana.as.personapp.common.setup;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;

/** Motores de persistencia disponibles. Agregar un motor nuevo solo requiere añadir una constante aquí. */
public enum DatabaseOption {
	MONGO("MongoDB"), MARIA("MariaDB");

	private final String displayName;

	DatabaseOption(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static DatabaseOption fromString(String value) throws InvalidOptionException {
		for (DatabaseOption option : values()) {
			if (option.name().equalsIgnoreCase(value))
				return option;
		}
		throw new InvalidOptionException("Invalid database option: " + value);
	}

	/** Nombre legible para respuestas; si el valor no es un motor conocido se devuelve tal cual. */
	public static String displayNameOf(String value) {
		for (DatabaseOption option : values()) {
			if (option.name().equalsIgnoreCase(value))
				return option.displayName;
		}
		return value;
	}
}
