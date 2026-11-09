
package co.edu.javeriana.as.personapp.common.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Validaciones de entrada compartidas por la API REST y la CLI. Lanzan IllegalArgumentException
 * (que la API traduce a HTTP 400) con un mensaje que indica el campo.
 * Los límites reflejan el esquema de MariaDB (ver scripts/persona_ddl_maria.sql).
 */
public final class Entrada {

	public static final int MAX_NOMBRE = 45;
	public static final int MAX_NOMBRE_PROFESION = 90;
	public static final int MAX_NUMERO_TELEFONO = 15;
	public static final int MAX_OPERADOR = 45;
	public static final int MAX_UNIVERSIDAD = 50;
	public static final int EDAD_MAXIMA = 150;

	private Entrada() {
	}

	public static Integer entero(String campo, String valor) {
		if (valor == null || valor.isBlank())
			throw new IllegalArgumentException(campo + " es obligatorio");
		try {
			return Integer.valueOf(valor.trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(campo + " debe ser un número entero");
		}
	}

	public static Integer positivo(String campo, Integer valor) {
		if (valor == null || valor <= 0)
			throw new IllegalArgumentException(campo + " debe ser un número mayor que cero");
		return valor;
	}

	public static Integer edadOpcional(String campo, String valor) {
		if (valor == null || valor.isBlank())
			return null;
		int edad = entero(campo, valor);
		if (edad < 0 || edad > EDAD_MAXIMA)
			throw new IllegalArgumentException(campo + " debe estar entre 0 y " + EDAD_MAXIMA);
		return edad;
	}

	public static String texto(String campo, String valor, int maximo) {
		if (valor == null || valor.isBlank())
			throw new IllegalArgumentException(campo + " es obligatorio");
		return textoOpcional(campo, valor, maximo);
	}

	public static String textoOpcional(String campo, String valor, int maximo) {
		if (valor == null)
			return null;
		String limpio = valor.trim();
		if (limpio.length() > maximo)
			throw new IllegalArgumentException(campo + " supera " + maximo + " caracteres");
		return limpio;
	}

	public static Integer rangoEdad(String campo, Integer valor) {
		if (valor != null && (valor < 0 || valor > EDAD_MAXIMA))
			throw new IllegalArgumentException(campo + " debe estar entre 0 y " + EDAD_MAXIMA);
		return valor;
	}

	public static LocalDate fechaOpcional(String campo, String valor) {
		if (valor == null || valor.isBlank())
			return null;
		try {
			return LocalDate.parse(valor.trim());
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(campo + " debe tener formato yyyy-MM-dd");
		}
	}
}
