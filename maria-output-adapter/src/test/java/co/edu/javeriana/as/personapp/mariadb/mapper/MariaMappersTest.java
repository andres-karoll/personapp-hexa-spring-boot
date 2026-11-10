package co.edu.javeriana.as.personapp.mariadb.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

class MariaMappersTest {

	@Test
	void profesionIdaYVuelta() {
		ProfesionMapperMaria mapper = new ProfesionMapperMaria();
		ProfesionEntity e = mapper.fromDomainToAdapter(new Profession(1, "Ingeniero"));
		assertThat(e.getId()).isEqualTo(1);
		assertThat(e.getDes()).isEqualTo("");
		Profession p = mapper.fromAdapterToDomain(new ProfesionEntity(2, "Medico"));
		assertThat(p.getName()).isEqualTo("Medico");
		assertThat(p.getDescription()).isEqualTo("");
	}

	@Test
	void personaShallowNoRecorreEstudiosNiTelefonos() {
		PersonaMapperMaria mapper = new PersonaMapperMaria();
		PersonaEntity pe = new PersonaEntity(5, "Ana", "Lopez", 'F');
		pe.setEdad(33);
		Person p = mapper.fromAdapterToDomainShallow(pe);
		assertThat(p.getGender()).isEqualTo(Gender.FEMALE);
		assertThat(p.getAge()).isEqualTo(33);
		assertThat(p.getStudies()).isNull();
		assertThat(p.getPhoneNumbers()).isNull();
	}

	@Test
	void telefonoGuardaSoloElIdDelDuenoYLeeElDuenioSuperficial() {
		TelefonoMapperMaria mapper = new TelefonoMapperMaria();
		ReflectionTestUtils.setField(mapper, "personaMapperMaria", new PersonaMapperMaria());
		Person owner = new Person(9, "Pepe", "Perez", Gender.MALE);
		TelefonoEntity te = mapper.fromDomainToAdapter(new Phone("3001", "Claro", owner));
		assertThat(te.getDuenio().getCc()).isEqualTo(9);
		TelefonoEntity lectura = new TelefonoEntity("3001", "Claro");
		lectura.setDuenio(new PersonaEntity(9, "Pepe", "Perez", 'M'));
		Phone phone = mapper.fromAdapterToDomain(lectura);
		assertThat(phone.getOwner().getFirstName()).isEqualTo("Pepe");
	}

	@Test
	void estudioGuardaClaveCompuestaYFechaLocalDate() {
		EstudiosMapperMaria mapper = new EstudiosMapperMaria();
		ReflectionTestUtils.setField(mapper, "personaMapperMaria", new PersonaMapperMaria());
		ReflectionTestUtils.setField(mapper, "profesionMapperMaria", new ProfesionMapperMaria());
		Study s = new Study(new Person(1, "A", "B", Gender.MALE), new Profession(3, "Abogado"));
		s.setGraduationDate(LocalDate.of(2021, 5, 6));
		EstudiosEntity e = mapper.fromDomainToAdapter(s);
		assertThat(e.getEstudiosPK().getCcPer()).isEqualTo(1);
		assertThat(e.getEstudiosPK().getIdProf()).isEqualTo(3);
		assertThat(e.getFecha()).isEqualTo(LocalDate.of(2021, 5, 6));
		assertThat(e.getUniver()).isEqualTo("");
	}

	@Test
	void estudioLecturaRecuperaPersonaYProfesion() {
		EstudiosMapperMaria mapper = new EstudiosMapperMaria();
		ReflectionTestUtils.setField(mapper, "personaMapperMaria", new PersonaMapperMaria());
		ReflectionTestUtils.setField(mapper, "profesionMapperMaria", new ProfesionMapperMaria());
		EstudiosEntity e = new EstudiosEntity(new EstudiosEntityPK(3, 1));
		e.setPersona(new PersonaEntity(1, "Pepe", "Perez", 'M'));
		e.setProfesion(new ProfesionEntity(3, "Abogado"));
		e.setFecha(LocalDate.of(2019, 2, 2));
		e.setUniver("U");
		Study s = mapper.fromAdapterToDomain(e);
		assertThat(s.getPerson().getFirstName()).isEqualTo("Pepe");
		assertThat(s.getProfession().getName()).isEqualTo("Abogado");
		assertThat(s.getGraduationDate()).isEqualTo(LocalDate.of(2019, 2, 2));
		assertThat(s.getUniversityName()).isEqualTo("U");
	}
}
