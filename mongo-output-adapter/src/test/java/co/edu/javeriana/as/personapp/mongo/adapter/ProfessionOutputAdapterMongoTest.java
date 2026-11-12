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
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;

@ExtendWith(MockitoExtension.class)
class ProfessionOutputAdapterMongoTest {
    @Mock ProfesionRepositoryMongo repository;
    @Mock ProfesionMapperMongo mapper;
    @Mock EstudiosRepositoryMongo studies;
    @InjectMocks ProfessionOutputAdapterMongo adapter;

    @Test
    void saveUsesRepositoryAndMapsResult() {
        Profession value = new Profession(7, "Ingenieria");
        ProfesionDocument entity = new ProfesionDocument();
        when(mapper.fromDomainToAdapter(value)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.save(value));
        verify(repository).save(entity);
    }

    @Test
    void findByIdUsesCorrectIdentifier() {
        ProfesionDocument entity = new ProfesionDocument();
        Profession value = new Profession(7, "Ingenieria");
        when(repository.findById(7)).thenReturn(Optional.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertSame(value, adapter.findById(7));
    }

    @Test
    void missingIdentifierReturnsNull() {
        assertNull(adapter.findById(7));
    }

    @Test
    void findMapsAllRecords() {
        ProfesionDocument entity = new ProfesionDocument();
        Profession value = new Profession(7, "Ingenieria");
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(value);
        assertEquals(List.of(value), adapter.find());
    }

    @Test
    void deleteUsesCorrectIdentifier() {
        assertTrue(adapter.delete(7));
        verify(repository).deleteById(7);
    }

    @Test
    void referencedProfessionCannotBeDeleted() {
        when(studies.existsByProfessionId(7)).thenReturn(true);
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> adapter.delete(7));
        verify(repository, never()).deleteById(any());
    }
}
