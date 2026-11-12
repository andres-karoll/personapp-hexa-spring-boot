package co.edu.javeriana.as.personapp.mariadb.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import co.edu.javeriana.as.personapp.domain.*;
import co.edu.javeriana.as.personapp.mariadb.entity.*;
import co.edu.javeriana.as.personapp.mariadb.repository.*;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;

@ExtendWith(MockitoExtension.class)
class StudyOutputAdapterMariaTest {
    @Mock EstudiosRepositoryMaria repository;
    @Mock EstudiosMapperMaria mapper;
    @Mock PersonaRepositoryMaria persons;
    @Mock ProfesionRepositoryMaria professions;
    @InjectMocks StudyOutputAdapterMaria adapter;

    @Test
    void saveUsesRepositoryAndMapsResult() {
        Study value = new Study(new Person(123, "Ana", "Perez", Gender.FEMALE), new Profession(7, "Ingenieria"));
        EstudiosEntity entity = new EstudiosEntity();
        when(persons.findById(123)).thenReturn(Optional.of(new PersonaEntity(123, "Ana", "Perez", 'F')));
        when(professions.findById(7)).thenReturn(Optional.of(new ProfesionEntity(7, "Ingenieria")));
        when(mapper.fromDomainToAdapter(value)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.save(value));
        verify(repository).save(entity);
    }

    @Test
    void findByIdUsesCorrectIdentifier() {
        EstudiosEntity entity = new EstudiosEntity();
        Study value = new Study(new Person(123, "Ana", "Perez", Gender.FEMALE), new Profession(7, "Ingenieria"));
        when(repository.findById(new EstudiosEntityPK(7, 123))).thenReturn(Optional.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.findById(7, 123));
    }

    @Test
    void missingIdentifierReturnsNull() {
        assertNull(adapter.findById(7, 123));
    }

    @Test
    void findMapsAllRecords() {
        EstudiosEntity entity = new EstudiosEntity();
        Study value = new Study(new Person(123, "Ana", "Perez", Gender.FEMALE), new Profession(7, "Ingenieria"));
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertEquals(List.of(value), adapter.find());
    }

    @Test
    void deleteUsesCorrectIdentifier() {
        assertTrue(adapter.delete(7, 123));
        verify(repository).deleteById(new EstudiosEntityPK(7, 123));
    }
}
