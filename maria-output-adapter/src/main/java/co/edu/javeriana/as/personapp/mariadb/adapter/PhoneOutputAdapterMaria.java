package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

@Adapter("phoneOutputAdapterMaria")
@javax.transaction.Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {
    @Autowired
    private TelefonoRepositoryMaria repository;
    @Autowired
    private TelefonoMapperMaria mapper;
    @Autowired
    private PersonaRepositoryMaria persons;

    @Override
    public Phone save(Phone value) {
        TelefonoEntity entity = mapper.fromDomainToAdapter(value);
        entity.setDuenio(persons.findById(value.getOwner().getIdentification())
            .orElseThrow(() -> new org.springframework.dao.DataIntegrityViolationException("Persona inexistente")));
        return mapper.fromAdapterToDomain(repository.save(entity));
    }

    @Override
    public Boolean delete(String number) {
        repository.deleteById(number);
        return !repository.existsById(number);
    }

    @Override
    public List<Phone> find() {
        return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        return repository.findById(number).map(mapper::fromAdapterToDomain).orElse(null);
    }
}
