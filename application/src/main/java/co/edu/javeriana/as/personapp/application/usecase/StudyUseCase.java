package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@UseCase
public class StudyUseCase implements StudyInputPort {

	private final StudyOutputPort studyPersistence;

	public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
		this.studyPersistence = studyPersistence;
	}

	@Override
	public Study create(Study study) throws AlreadyExistException {
		if (studyPersistence.findById(study.getPerson().getIdentification(),
				study.getProfession().getIdentification()) != null)
			throw new AlreadyExistException("The study of person " + study.getPerson().getIdentification()
					+ " in profession " + study.getProfession().getIdentification() + " already exists");
		return studyPersistence.save(study);
	}

	@Override
	public Study edit(Integer personIdentification, Integer professionIdentification, Study study)
			throws NoExistException {
		if (studyPersistence.findById(personIdentification, professionIdentification) == null)
			throw new NoExistException("The study of person " + personIdentification + " in profession "
					+ professionIdentification + " does not exist into db, cannot be edited");
		return studyPersistence.save(study);
	}

	@Override
	public Boolean drop(Integer personIdentification, Integer professionIdentification) throws NoExistException {
		if (studyPersistence.findById(personIdentification, professionIdentification) == null)
			throw new NoExistException("The study of person " + personIdentification + " in profession "
					+ professionIdentification + " does not exist into db, cannot be dropped");
		return studyPersistence.delete(personIdentification, professionIdentification);
	}

	@Override
	public List<Study> findAll() {
		return studyPersistence.find();
	}

	@Override
	public Study findOne(Integer personIdentification, Integer professionIdentification) throws NoExistException {
		Study study = studyPersistence.findById(personIdentification, professionIdentification);
		if (study == null)
			throw new NoExistException("The study of person " + personIdentification + " in profession "
					+ professionIdentification + " does not exist into db, cannot be found");
		return study;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
