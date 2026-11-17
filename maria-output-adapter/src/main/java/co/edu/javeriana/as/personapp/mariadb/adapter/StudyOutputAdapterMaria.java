package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional

public class StudyOutputAdapterMaria implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMaria repository;

	@Autowired
	private EstudiosMapperMaria mapper;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MariaDB (study)");
		repository.save(mapper.fromDomainToAdapter(study));
		return study;
	}

	@Override
	public Boolean delete(Integer personIdentification, Integer professionIdentification) {
		EstudiosEntityPK pk = new EstudiosEntityPK(professionIdentification, personIdentification);
		repository.deleteById(pk);
		return repository.findById(pk).isEmpty();
	}

	@Override
	public List<Study> find() {
		return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personIdentification, Integer professionIdentification) {
		return repository.findById(new EstudiosEntityPK(professionIdentification, personIdentification))
				.map(mapper::fromAdapterToDomain).orElse(null);
	}
}

