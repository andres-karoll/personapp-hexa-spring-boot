package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;

@Adapter("professionOutputAdapterMaria")
@javax.transaction.Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMaria repository;

    @Autowired
    private ProfesionMapperMaria mapper;

    @Override
    public Profession save(Profession profession) {
        return mapper.fromAdapterToDomain(
            repository.save(mapper.fromDomainToAdapter(profession))
        );
    }

    @Override
    public Boolean delete(Integer identification) {
        repository.deleteById(identification);
        return !repository.existsById(identification);
    }

    @Override
    public List<Profession> find() {
        return repository.findAll().stream()
            .map(mapper::fromAdapterToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        return repository.findById(identification)
            .map(mapper::fromAdapterToDomain)
            .orElse(null);
    }
}