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
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;

@ExtendWith(MockitoExtension.class)
class PhoneOutputAdapterMariaTest {
    @Mock TelefonoRepositoryMaria repository;
    @Mock TelefonoMapperMaria mapper;
    @Mock PersonaRepositoryMaria persons;
    @InjectMocks PhoneOutputAdapterMaria adapter;

    @Test
    void saveUsesRepositoryAndMapsResult() {
        Phone value = new Phone("300123", "Operador", new Person(123, "Ana", "Perez", Gender.FEMALE));
        TelefonoEntity entity = new TelefonoEntity();
        when(persons.findById(123)).thenReturn(Optional.of(new PersonaEntity(123, "Ana", "Perez", 'F')));
        when(mapper.fromDomainToAdapter(value)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.save(value));
        verify(repository).save(entity);
    }

    @Test
    void findByIdUsesCorrectIdentifier() {
        TelefonoEntity entity = new TelefonoEntity();
        Phone value = new Phone("300123", "Operador", new Person(123, "Ana", "Perez", Gender.FEMALE));
        when(repository.findById("300123")).thenReturn(Optional.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.findById("300123"));
    }

    @Test
    void missingIdentifierReturnsNull() {
        assertNull(adapter.findById("300123"));
    }

    @Test
    void findMapsAllRecords() {
        TelefonoEntity entity = new TelefonoEntity();
        Phone value = new Phone("300123", "Operador", new Person(123, "Ana", "Perez", Gender.FEMALE));
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertEquals(List.of(value), adapter.find());
    }

    @Test
    void deleteUsesCorrectIdentifier() {
        assertTrue(adapter.delete("300123"));
        verify(repository).deleteById("300123");
    }
}
