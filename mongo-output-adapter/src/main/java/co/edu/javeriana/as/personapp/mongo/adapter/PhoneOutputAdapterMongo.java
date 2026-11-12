package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {
    @Autowired
    private TelefonoRepositoryMongo repository;
    @Autowired
    private TelefonoMapperMongo mapper;
    @Autowired
    private PersonaRepositoryMongo persons;

    @Override
    public Phone save(Phone value) {
        if (!persons.existsById(value.getOwner().getIdentification())) {
            throw new org.springframework.dao.DataIntegrityViolationException("Referencia inexistente");
        }
        return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(value)));
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
