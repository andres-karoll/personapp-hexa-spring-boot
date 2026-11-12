package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {
    @Autowired
    private ProfesionRepositoryMongo repository;
    @Autowired
    private ProfesionMapperMongo mapper;
    @Autowired
    private EstudiosRepositoryMongo studies;

    @Override
    public Profession save(Profession value) {
        return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(value)));
    }

    @Override
    public Boolean delete(Integer identification) {
        if (studies.existsByProfessionId(identification)) {
            throw new org.springframework.dao.DataIntegrityViolationException("Profesion con estudios asociados");
        }
        repository.deleteById(identification);
        return !repository.existsById(identification);
    }

    @Override
    public List<Profession> find() {
        return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        return repository.findById(identification).map(mapper::fromAdapterToDomain).orElse(null);
    }
}
