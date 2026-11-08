package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import org.springframework.dao.DataIntegrityViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")

public class StudyOutputAdapterMongo implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMongo repository;

	@Autowired
	private PersonaRepositoryMongo personaRepository;

	@Autowired
	private ProfesionRepositoryMongo profesionRepository;

	@Autowired
	private EstudiosMapperMongo mapper;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MongoDB (study)");
		if (!personaRepository.existsById(study.getPerson().getIdentification()))
			throw new DataIntegrityViolationException(
					"The person " + study.getPerson().getIdentification() + " does not exist");
		if (!profesionRepository.existsById(study.getProfession().getIdentification()))
			throw new DataIntegrityViolationException(
					"The profession " + study.getProfession().getIdentification() + " does not exist");
		EstudiosDocument saved = repository.save(mapper.fromDomainToAdapter(study));
		return mapper.fromAdapterToDomain(repository.findById(saved.getId()).orElseThrow());
	}

	@Override
	public Boolean delete(Integer personIdentification, Integer professionIdentification) {
		String id = EstudiosMapperMongo.buildId(personIdentification, professionIdentification);
		repository.deleteById(id);
		return repository.findById(id).isEmpty();
	}

	@Override
	public List<Study> find() {
		return repository.findAll().stream().map(mapper::fromAdapterToDomain).collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personIdentification, Integer professionIdentification) {
		return repository.findById(EstudiosMapperMongo.buildId(personIdentification, professionIdentification))
				.map(mapper::fromAdapterToDomain).orElse(null);
	}
}

