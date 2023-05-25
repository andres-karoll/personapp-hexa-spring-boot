package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersintence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersintence) {
        this.studyPersintence=studyPersintence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersintence= studyPersistence;
    }

    @Override
    public Study create(Study person) {
        log.debug("Into create on Application Domain");
        return studyPersintence.save(person);
    }

    @Override
    public Study edit(Integer identification, Study study) throws NoExistException {
        Study oldStudy = studyPersintence.findById(identification);
        if (oldStudy != null)
            return studyPersintence.save(study);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Study oldStudy = studyPersintence.findById(identification);
        if (oldStudy != null)
            return studyPersintence.delete(identification);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Study> findAll() {
        log.info("Output: " + studyPersintence.getClass());
        return studyPersintence.find();
    }

    @Override
    public Study findOne(Integer identification) throws NoExistException {
        Study oldStudy = studyPersintence.findById(identification);
        if (oldStudy != null)
            return oldStudy;
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getPerson(Integer identification) throws NoExistException {
        Study study = studyPersintence.findById(identification);
        if (study != null)
            return study.getPerson();
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }

    @Override
    public Profession getProfession(Integer identification) throws NoExistException {
        Study study = studyPersintence.findById(identification);
        if (study != null)
            return study.getProfession();
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }
}
