package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional

public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMaria repository;

	@Autowired
	private ProfesionMapperMaria mapper;

	@Override
	public Profession save(Profession profession) {
		log.debug("Into save on Adapter MariaDB (profession)");
		return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(profession)));
	}

	@Override
	public Boolean delete(Integer identification) {
		repository.deleteById(identification);
		return repository.findById(identification).isEmpty();
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

