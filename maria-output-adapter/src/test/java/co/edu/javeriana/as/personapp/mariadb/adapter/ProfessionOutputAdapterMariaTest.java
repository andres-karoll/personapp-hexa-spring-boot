package co.edu.javeriana.as.personapp.mariadb.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;

@ExtendWith(MockitoExtension.class)
class ProfessionOutputAdapterMariaTest {

    @Mock
    private ProfesionRepositoryMaria repository;

    @Mock
    private ProfesionMapperMaria mapper;

    @InjectMocks
    private ProfessionOutputAdapterMaria adapter;

    private Profession profession;
    private ProfesionEntity entity;

    @BeforeEach
    void setUp() {
        profession = new Profession();
        profession.setIdentification(1);
        profession.setName("Ingeniería");

        entity = new ProfesionEntity(1, "Ingeniería");
    }

    @Test
    void saveProfession() {
        when(mapper.fromDomainToAdapter(profession)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromAdapterToDomain(entity)).thenReturn(profession);

        Profession result = adapter.save(profession);

        assertEquals(1, result.getIdentification());
        verify(repository).save(entity);
    }

    @Test
    void findExistingProfession() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.fromAdapterToDomain(entity)).thenReturn(profession);

        Profession result = adapter.findById(1);

        assertNotNull(result);
        assertEquals("Ingeniería", result.getName());
    }

    @Test
    void findNonExistingProfession() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertNull(adapter.findById(99));
    }
    @Test
void findAllProfessions() {
    when(repository.findAll()).thenReturn(java.util.List.of(entity));
    when(mapper.fromAdapterToDomain(entity)).thenReturn(profession);

    var result = adapter.find();

    assertEquals(1, result.size());
    assertEquals("Ingeniería", result.get(0).getName());
}

@Test
void deleteProfession() {
    when(repository.existsById(1)).thenReturn(false);

    assertTrue(adapter.delete(1));

    verify(repository).deleteById(1);
}
}
