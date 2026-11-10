package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import org.springframework.dao.DataIntegrityViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMongo")

public class PhoneOutputAdapterMongo implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMongo repository;

	@Autowired
	private PersonaRepositoryMongo personaRepository;

	@Autowired
	private TelefonoMapperMongo mapper;

	@Override
	public Phone save(Phone phone) {
		log.debug("Into save on Adapter MongoDB (phone)");
		if (!personaRepository.existsById(phone.getOwner().getIdentification()))
			throw new DataIntegrityViolationException(
					"The owner " + phone.getOwner().getIdentification() + " does not exist");
		TelefonoDocument saved = repository.save(mapper.fromDomainToAdapter(phone));
		return mapper.fromAdapterToDomain(repository.findById(saved.getId()).orElseThrow());
	}

	@Override
	public Boolean delete(String number) {
		repository.deleteById(number);
		return repository.findById(number).isEmpty();
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

