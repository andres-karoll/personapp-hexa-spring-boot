package co.edu.javeriana.as.personapp.mariadb.adapter;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import co.edu.javeriana.as.personapp.domain.*;
import co.edu.javeriana.as.personapp.mariadb.entity.*;
import co.edu.javeriana.as.personapp.mariadb.repository.*;

@EnabledIfSystemProperty(named = "persistence.integration", matches = "true")
@SpringBootTest(classes = PersistenceIntegrationTest.Config.class, properties = {
    "logging.level.root=WARN", "spring.main.allow-circular-references=true", "spring.datasource.url=${P2_MARIA_URL:jdbc:mariadb://localhost:3307/persona_db}",
    "spring.datasource.username=${P2_MARIA_USER:persona_db}", "spring.datasource.password=${P2_MARIA_PASSWORD:persona_db}",
    "spring.jpa.hibernate.ddl-auto=none"
})
class PersistenceIntegrationTest {
    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = {"co.edu.javeriana.as.personapp.mariadb.adapter", "co.edu.javeriana.as.personapp.mariadb.mapper"})
    @org.springframework.data.jpa.repository.config.EnableJpaRepositories("co.edu.javeriana.as.personapp.mariadb.repository")
    @org.springframework.boot.autoconfigure.domain.EntityScan("co.edu.javeriana.as.personapp.mariadb.entity")
    static class Config {}

    @Autowired ProfessionOutputAdapterMaria professions;
    @Autowired StudyOutputAdapterMaria studies;
    @Autowired PhoneOutputAdapterMaria phones;
    @Autowired PersonOutputAdapterMaria persons;
    @Autowired PersonaRepositoryMaria personsRepository;
    @Autowired EstudiosRepositoryMaria studiesRepository;
    @Autowired TelefonoRepositoryMaria phonesRepository;
    @Autowired ProfesionRepositoryMaria professionsRepository;
    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbc;

    @Test
    void realCrudRelationshipsAndReferentialIntegrity() {
        int personId = -Math.abs(UUID.randomUUID().hashCode() % 1000000000) - 1;
        int professionId = personId - 1;
        int otherProfessionId = personId - 2;
        String number = "T" + Math.abs(personId);
        Person person = new Person(personId, "Prueba", "Persona2", Gender.FEMALE);
        Profession profession = new Profession(professionId, "Prueba Persona2");
        try {
            assertNull(professions.findById(professionId));
            assertNull(phones.findById(number));
            assertNull(studies.findById(professionId, personId));
            professions.save(profession);
            professions.save(new Profession(otherProfessionId, "Otra profesion Persona2"));
            assertThrows(DataIntegrityViolationException.class, () -> phones.save(new Phone(number, "Operador", person)));
            assertThrows(DataIntegrityViolationException.class, () -> studies.save(new Study(person, profession)));
            personsRepository.save(new PersonaEntity(personId, "Prueba", "Persona2", 'F'));
            assertEquals(personId, persons.findById(personId).getIdentification());
            profession.setDescription("Actualizada");
            assertEquals("Actualizada", professions.save(profession).getDescription());
            assertEquals("Actualizada", professions.findById(professionId).getDescription());
            assertTrue(professions.find().stream().anyMatch(p -> p.getIdentification().equals(professionId)));

            Phone phone = new Phone(number, "Operador", person);
            phones.save(phone);
            phone.setCompany("Actualizado");
            phones.save(phone);
            assertEquals("Actualizado", phones.findById(number).getCompany());
            assertEquals(personId, phones.findById(number).getOwner().getIdentification());
            assertTrue(phones.find().stream().anyMatch(p -> p.getNumber().equals(number)));
            assertTrue(phonesRepository.existsById(number));

            Study study = new Study(person, profession, LocalDate.of(2024, 2, 29), "Javeriana");
            studies.save(study);
            study.setUniversityName("Actualizada");
            studies.save(study);
            studies.save(new Study(person, new Profession(otherProfessionId, "Otra profesion Persona2")));
            assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "INSERT INTO estudios(id_prof,cc_per) VALUES (?,?)", professionId, personId));
            assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "INSERT INTO estudios(id_prof,cc_per) VALUES (?,?)", professionId, 0));
            assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "INSERT INTO estudios(id_prof,cc_per) VALUES (?,?)", 0, personId));
            assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "INSERT INTO telefono(num,oper,duenio) VALUES (?,?,?)", number + "X", "Prueba", 0));
            Study loaded = studies.findById(professionId, personId);
            assertEquals("Actualizada", loaded.getUniversityName());
            assertEquals(study.getGraduationDate(), loaded.getGraduationDate());
            assertEquals(personId, loaded.getPerson().getIdentification());
            assertEquals(professionId, loaded.getProfession().getIdentification());
            assertTrue(studies.find().stream().anyMatch(s -> s.getPerson().getIdentification().equals(personId)));
            assertEquals(2, persons.findById(personId).getStudies().size());
            assertEquals(1, persons.findById(personId).getPhoneNumbers().size());
            assertEquals(1, professions.findById(professionId).getStudies().size());
            assertThrows(DataIntegrityViolationException.class, () -> professions.delete(professionId));
            assertThrows(DataIntegrityViolationException.class, () -> persons.delete(personId));
            assertNotNull(studies.findById(professionId, personId));
            assertNotNull(phones.findById(number));
            assertTrue(studies.delete(professionId, personId));
            assertNull(studies.findById(professionId, personId));
            assertNotNull(studies.findById(otherProfessionId, personId));
            assertNull(studies.findById(otherProfessionId, personId).getGraduationDate());
            assertNotNull(persons.findById(personId));
            assertNotNull(professions.findById(professionId));
            assertTrue(studies.delete(otherProfessionId, personId));
            assertTrue(professions.delete(otherProfessionId));
            assertTrue(phones.delete(number));
            assertNull(phones.findById(number));
            assertTrue(professions.delete(professionId));
            assertNull(professions.findById(professionId));
            assertTrue(persons.delete(personId));
        } finally {
            if (studiesRepository.existsById(new co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK(professionId, personId)))
                studiesRepository.deleteById(new co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK(professionId, personId));
            co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK otherKey =
                new co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK(otherProfessionId, personId);
            if (studiesRepository.existsById(otherKey)) studiesRepository.deleteById(otherKey);
            if (professionsRepository.existsById(otherProfessionId)) professionsRepository.deleteById(otherProfessionId);
            if (phonesRepository.existsById(number)) phonesRepository.deleteById(number);
            if (professionsRepository.existsById(professionId)) professionsRepository.deleteById(professionId);
            if (personsRepository.existsById(personId)) personsRepository.deleteById(personId);
        }
    }
}
