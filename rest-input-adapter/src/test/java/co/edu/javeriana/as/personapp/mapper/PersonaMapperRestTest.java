package co.edu.javeriana.as.personapp.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;

class PersonaMapperRestTest {

	private final PersonaMapperRest mapper = new PersonaMapperRest();

	@Test
	void convierteUnaPeticionValida() {
		Person p = mapper.fromAdapterToDomain(new PersonaRequest("123", " Ana ", "Lopez", "30", "F", "maria"));
		assertThat(p.getIdentification()).isEqualTo(123);
		assertThat(p.getFirstName()).isEqualTo("Ana");
		assertThat(p.getGender()).isEqualTo(Gender.FEMALE);
		assertThat(p.getAge()).isEqualTo(30);
	}

	@Test
	void edadVaciaQuedaNula() {
		Person p = mapper.fromAdapterToDomain(new PersonaRequest("1", "A", "B", "", "M", "maria"));
		assertThat(p.getAge()).isNull();
		assertThat(p.getGender()).isEqualTo(Gender.MALE);
	}

	@Test
	void rechazaSexoDistintoDeMOF() {
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("1", "A", "B", "1", "X", "maria")))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("M o F");
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("1", "A", "B", "1", null, "maria")))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rechazaDniNoNumericoONegativo() {
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("abc", "A", "B", "1", "M", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("-1", "A", "B", "1", "M", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rechazaEdadFueraDeRango() {
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("1", "A", "B", "-5", "M", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("1", "A", "B", "151", "M", "maria")))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rechazaNombreMayorA45() {
		assertThatThrownBy(() -> mapper.fromAdapterToDomain(new PersonaRequest("1", "x".repeat(46), "B", "1", "M", "maria")))
				.hasMessageContaining("supera 45");
	}

	@Test
	void respuestaUsaEtiquetaDelMotorYEdadVaciaCuandoEsNula() {
		Person p = new Person(7, "Ana", "Lopez", Gender.FEMALE);
		var respuesta = mapper.fromDomainToAdapterRestMaria(p);
		assertThat(respuesta.getDatabase()).isEqualTo("MariaDB");
		assertThat(respuesta.getAge()).isEmpty();
		assertThat(respuesta.getSex()).isEqualTo("FEMALE");
		assertThat(mapper.fromDomainToAdapterRestMongo(p).getDatabase()).isEqualTo("MongoDB");
	}
}
