package co.edu.javeriana.as.personapp.common.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class EntradaTest {

	@Test
	void enteroRechazaVacioYNoNumerico() {
		assertThatThrownBy(() -> Entrada.entero("dni", " ")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> Entrada.entero("dni", "abc")).hasMessageContaining("número entero");
		assertThat(Entrada.entero("dni", " 42 ")).isEqualTo(42);
	}

	@Test
	void positivoRechazaCeroYNegativos() {
		assertThatThrownBy(() -> Entrada.positivo("id", 0)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> Entrada.positivo("id", -1)).isInstanceOf(IllegalArgumentException.class);
		assertThat(Entrada.positivo("id", 1)).isEqualTo(1);
	}

	@Test
	void textoRespetaLongitudMaxima() {
		assertThat(Entrada.texto("nombre", " Ana ", 45)).isEqualTo("Ana");
		assertThatThrownBy(() -> Entrada.texto("nombre", "x".repeat(46), 45))
				.hasMessageContaining("supera 45 caracteres");
		assertThatThrownBy(() -> Entrada.texto("nombre", null, 45)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void textoOpcionalAceptaVacio() {
		assertThat(Entrada.textoOpcional("universidad", null, 50)).isNull();
		assertThat(Entrada.textoOpcional("universidad", "U", 50)).isEqualTo("U");
	}

	@Test
	void edadEntreCeroYLimite() {
		assertThat(Entrada.edadOpcional("edad", "")).isNull();
		assertThat(Entrada.edadOpcional("edad", "30")).isEqualTo(30);
		assertThatThrownBy(() -> Entrada.edadOpcional("edad", "-5")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> Entrada.rangoEdad("edad", 151)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void fechaOpcionalValidaFormatoIso() {
		assertThat(Entrada.fechaOpcional("fecha", "2020-01-15")).isEqualTo(LocalDate.of(2020, 1, 15));
		assertThat(Entrada.fechaOpcional("fecha", null)).isNull();
		assertThatThrownBy(() -> Entrada.fechaOpcional("fecha", "2020/01/15"))
				.hasMessageContaining("yyyy-MM-dd");
	}
}
