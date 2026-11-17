package co.edu.javeriana.as.personapp.mongo.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

class MongoMappersTest {

	@Test
	void profesionIdaYVuelta() {
		ProfesionMapperMongo mapper = new ProfesionMapperMongo();
		ProfesionDocument d = mapper.fromDomainToAdapter(new Profession(4, "Medico"));
		assertThat(d.getId()).isEqualTo(4);
		assertThat(d.getDes()).isEqualTo("");
		assertThat(mapper.fromAdapterToDomain(new ProfesionDocument(4, "Medico", "Cura", null)).getDescription())
				.isEqualTo("Cura");
	}

	@Test
	void personaShallowSinListasAnidadas() {
		PersonaDocument pd = new PersonaDocument();
		pd.setId(8);
		pd.setNombre("Ana");
		pd.setApellido("Lopez");
		pd.setGenero("F");
		Person p = new PersonaMapperMongo().fromAdapterToDomainShallow(pd);
		assertThat(p.getGender()).isEqualTo(Gender.FEMALE);
		assertThat(p.getStudies()).isNull();
		assertThat(p.getPhoneNumbers()).isNull();
	}

	@Test
	void telefonoGuardaReferenciaPorIdYLeeDuenio() {
		TelefonoMapperMongo mapper = new TelefonoMapperMongo();
		ReflectionTestUtils.setField(mapper, "personaMapperMongo", new PersonaMapperMongo());
		TelefonoDocument td = mapper.fromDomainToAdapter(new Phone("3002", "Tigo", new Person(9, "P", "Q", Gender.MALE)));
		assertThat(td.getPrimaryDuenio().getId()).isEqualTo(9);
		PersonaDocument owner = new PersonaDocument();
		owner.setId(9);
		owner.setNombre("Pepe");
		owner.setApellido("Perez");
		owner.setGenero("M");
		Phone phone = mapper.fromAdapterToDomain(new TelefonoDocument("3002", "Tigo", owner));
		assertThat(phone.getOwner().getFirstName()).isEqualTo("Pepe");
	}

	@Test
	void idDeEstudioEsPersonaGuionProfesion() {
		assertThat(EstudiosMapperMongo.buildId(123, 4)).isEqualTo("123-4");
	}

	@Test
	void estudioIdaYVueltaConFechaYReferencias() {
		EstudiosMapperMongo mapper = new EstudiosMapperMongo();
		ReflectionTestUtils.setField(mapper, "personaMapperMongo", new PersonaMapperMongo());
		ReflectionTestUtils.setField(mapper, "profesionMapperMongo", new ProfesionMapperMongo());
		Study s = new Study(new Person(1, "A", "B", Gender.MALE), new Profession(2, "C"));
		s.setGraduationDate(LocalDate.of(2020, 4, 4));
		EstudiosDocument d = mapper.fromDomainToAdapter(s);
		assertThat(d.getId()).isEqualTo("1-2");
		assertThat(d.getPrimaryPersona().getId()).isEqualTo(1);
		assertThat(d.getPrimaryProfesion().getId()).isEqualTo(2);
		// En una lectura real la referencia llega con los datos de la persona cargados
		d.getPrimaryPersona().setNombre("A");
		d.getPrimaryPersona().setApellido("B");
		d.getPrimaryPersona().setGenero("M");
		d.getPrimaryProfesion().setNom("C");
		Study back = mapper.fromAdapterToDomain(d);
		assertThat(back.getGraduationDate()).isEqualTo(LocalDate.of(2020, 4, 4));
	}
}
