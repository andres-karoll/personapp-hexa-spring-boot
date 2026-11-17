package co.edu.javeriana.as.personapp.web;

import java.time.DateTimeException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;

/** Utilidades compartidas por las pantallas web. */
final class WebSupport {

	static final String MARIA = "MARIA";
	static final String MONGO = "MONGO";

	private WebSupport() {
	}

	/** Normaliza el motor elegido en la pantalla; cualquier valor desconocido usa MariaDB. */
	static String motor(String db) {
		return MONGO.equalsIgnoreCase(db) ? MONGO : MARIA;
	}

	static String nombreMotor(String db) {
		return MONGO.equals(motor(db)) ? "MongoDB" : "MariaDB";
	}

	/** Traduce una excepción a un mensaje legible para el usuario. */
	static String mensaje(Exception e) {
		if (e instanceof AlreadyExistException)
			return "Ya existe un registro con esa llave.";
		if (e instanceof NoExistException)
			return "El registro no existe.";
		if (e instanceof InvalidOptionException)
			return "Motor de base de datos no válido.";
		if (e instanceof DataIntegrityViolationException || e instanceof ObjectRetrievalFailureException)
			return "Llave duplicada o referencia inexistente (llave foránea).";
		if (e instanceof IllegalArgumentException || e instanceof DateTimeException || e instanceof NullPointerException)
			return e.getMessage() != null ? e.getMessage() : "Datos no válidos.";
		return "Error inesperado: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
	}
}
