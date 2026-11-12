package co.edu.javeriana.as.personapp.mongo.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import co.edu.javeriana.as.personapp.domain.*;
import co.edu.javeriana.as.personapp.mongo.document.*;
import co.edu.javeriana.as.personapp.mongo.repository.*;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;

@ExtendWith(MockitoExtension.class)
class PhoneOutputAdapterMongoTest {
    @Mock TelefonoRepositoryMongo repository;
    @Mock TelefonoMapperMongo mapper;
    @Mock PersonaRepositoryMongo persons;
    @InjectMocks PhoneOutputAdapterMongo adapter;

    @Test
    void saveUsesRepositoryAndMapsResult() {
        Phone value = new Phone("300123", "Operador", new Person(123, "Ana", "Perez", Gender.FEMALE));
        TelefonoDocument entity = new TelefonoDocument();
        when(persons.existsById(123)).thenReturn(true);
        when(mapper.fromDomainToAdapter(value)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.save(value));
        verify(repository).save(entity);
    }

    @Test
    void findByIdUsesCorrectIdentifier() {
        TelefonoDocument entity = new TelefonoDocument();
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
        TelefonoDocument entity = new TelefonoDocument();
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
