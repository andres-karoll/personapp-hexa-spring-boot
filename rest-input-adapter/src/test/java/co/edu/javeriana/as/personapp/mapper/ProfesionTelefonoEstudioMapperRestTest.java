package co.edu.javeriana.as.personapp.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;

class ProfesionTelefonoEstudioMapperRestTest {

	@Test
	void profesionValidaYRechazaNombreVacio() {
		ProfesionMapperRest mapper = new ProfesionMapperRest();
		Profession p = mapper.fromAdapterToDomain(10, new ProfesionRequest("10", "Arquitecto", "Diseña", "maria"));
		assertThat(p.getIdentification()).isEqualTo(10);
		assertThat(mapper.fromDomainToAdapterRest(p, "mongo").getDatabase()).isEqualTo("MongoDB");
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(10, new ProfesionRequest("10", " ", "d", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(0, new ProfesionRequest("0", "X", "d", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void telefonoValidoYRechazaNumeroVacioOMuyLargo() {
		TelefonoMapperRest mapper = new TelefonoMapperRest();
		Phone t = mapper.fromAdapterToDomain("3001112233", new TelefonoRequest("3001112233", "Claro", "123", "maria"));
		assertThat(t.getOwner().getIdentification()).isEqualTo(123);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain("", new TelefonoRequest("", "Claro", "123", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain("1".repeat(16), new TelefonoRequest("", "Claro", "123", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain("300", new TelefonoRequest("", "Claro", "", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void estudioConvierteFechaIsoYRechazaFechaMalFormada() {
		EstudioMapperRest mapper = new EstudioMapperRest();
		Study s = mapper.fromAdapterToDomain(1, 2, new EstudioRequest("1", "2", "2020-01-15", "U", "maria"));
		assertThat(s.getGraduationDate()).isEqualTo(LocalDate.of(2020, 1, 15));
		assertThat(mapper.fromDomainToAdapterRest(s, "maria").getFecha()).isEqualTo("2020-01-15");
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(1, 2, new EstudioRequest("1", "2", "2020/01/15", "U", "maria")))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("yyyy-MM-dd");
	}

	@Test
	void estudioSinFechaNiUniversidadEsValido() {
		Study s = new EstudioMapperRest().fromAdapterToDomain(1, 2, new EstudioRequest("1", "2", "", null, "mongo"));
		assertThat(s.getGraduationDate()).isNull();
		assertThat(s.getUniversityName()).isNull();
		assertThat(s.getPerson().getIdentification()).isEqualTo(1);
		assertThat(new Person().getIdentification()).isNull();
	}
}
