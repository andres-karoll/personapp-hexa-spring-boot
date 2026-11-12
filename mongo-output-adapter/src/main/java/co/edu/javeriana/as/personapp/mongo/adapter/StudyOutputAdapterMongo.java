package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;

@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {
    @Autowired
    private EstudiosRepositoryMongo repository;
    @Autowired
    private EstudiosMapperMongo mapper;
    @Autowired
    private PersonaRepositoryMongo persons;
    @Autowired
    private ProfesionRepositoryMongo professions;

    @Override
    public Study save(Study value) {
        if (!persons.existsById(value.getPerson().getIdentification()) || !professions.existsById(value.getProfession().getIdentification())) {
            throw new org.springframework.dao.DataIntegrityViolationException("Referencia inexistente");
        }
        return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(value)));
    }

    @Override
    public Boolean delete(Integer professionId, Integer personId) {
        repository.deleteById(personId + "-" + professionId);
        return !repository.existsById(personId + "-" + professionId);
    }

    @Override
    public List<Study> find() {
        return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer professionId, Integer personId) {
        return repository.findById(personId + "-" + professionId).map(mapper::fromAdapterToDomain).orElse(null);
    }
}
