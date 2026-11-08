package co.edu.javeriana.as.personapp.common.setup;

import java.util.EnumMap;
import java.util.Map;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;

/**
 * Asocia cada motor de persistencia con su puerto de salida. Los adaptadores de entrada dependen de
 * este registro (abstracción) en vez de conocer MariaDB o MongoDB (inversión de dependencias).
 */
public class PersistenceRegistry<T> {

	private final Map<DatabaseOption, T> ports = new EnumMap<>(DatabaseOption.class);

	public PersistenceRegistry<T> register(DatabaseOption option, T port) {
		ports.put(option, port);
		return this;
	}

	public T resolve(String database) throws InvalidOptionException {
		DatabaseOption option = DatabaseOption.fromString(database);
		T port = ports.get(option);
		if (port == null)
			throw new InvalidOptionException("No persistence adapter registered for: " + database);
		return port;
	}
}
