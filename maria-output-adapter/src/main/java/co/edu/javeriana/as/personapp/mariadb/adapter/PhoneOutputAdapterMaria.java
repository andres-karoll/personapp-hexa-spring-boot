package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional

public class PhoneOutputAdapterMaria implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMaria repository;

	@Autowired
	private TelefonoMapperMaria mapper;

	@Override
	public Phone save(Phone phone) {
		log.debug("Into save on Adapter MariaDB (phone)");
		return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(phone)));
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

