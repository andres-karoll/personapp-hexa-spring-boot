package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")

public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMongo repository;

	@Autowired
	private ProfesionMapperMongo mapper;

	@Autowired
	private EstudiosRepositoryMongo studyRepository;

	@Override
	public Profession save(Profession profession) {
		log.debug("Into save on Adapter MongoDB (profession)");
		return mapper.fromAdapterToDomain(repository.save(mapper.fromDomainToAdapter(profession)));
	}

	@Override
	public Boolean delete(Integer identification) {
		studyRepository.deleteByProfesion(identification);
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

