package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Adapter("studyOutputAdapterMaria")
@javax.transaction.Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {
    @Autowired
    private EstudiosRepositoryMaria repository;
    @Autowired
    private EstudiosMapperMaria mapper;
    @Autowired
    private PersonaRepositoryMaria persons;
    @Autowired
    private ProfesionRepositoryMaria professions;

    @Override
    public Study save(Study value) {
        EstudiosEntity entity = mapper.fromDomainToAdapter(value);
        entity.setPersona(persons.findById(value.getPerson().getIdentification())
            .orElseThrow(() -> new org.springframework.dao.DataIntegrityViolationException("Persona inexistente")));
        entity.setProfesion(professions.findById(value.getProfession().getIdentification())
            .orElseThrow(() -> new org.springframework.dao.DataIntegrityViolationException("Profesion inexistente")));
        return mapper.fromAdapterToDomain(repository.save(entity));
    }

    @Override
    public Boolean delete(Integer professionId, Integer personId) {
        repository.deleteById(new EstudiosEntityPK(professionId, personId));
        return !repository.existsById(new EstudiosEntityPK(professionId, personId));
    }

    @Override
    public List<Study> find() {
        return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer professionId, Integer personId) {
        return repository.findById(new EstudiosEntityPK(professionId, personId)).map(mapper::fromAdapterToDomain).orElse(null);
    }
}
