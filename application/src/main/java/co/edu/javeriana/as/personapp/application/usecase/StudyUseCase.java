package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.pk.EstudiosEntityPK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {
    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("estudioOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence=studyPersistence;
    }

    public void setPersintence(StudyOutputPort studyPersistence) {
        this.studyPersistence=studyPersistence;
    }

    public Study create(Study profession) {
        log.debug("Into create on Application Domain");
        return studyPersistence.save(profession);
    }

    @Override
    public Study edit(Integer identification, Study profession) throws NoExistException {
        return null;
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        return null;
    }

    @Override
    public List<Study> findAll() {
        return null;
    }

    @Override
    public Study findOne(Integer identification) throws NoExistException {
        return null;
    }
//
//    public Study edit(EstudiosEntityPK identification, Study profession) throws NoExistException {
//        Study oldStudy = studyPersistence.findById(identification);
//        if (Study != null)
//            return studyPersistence.save(profession);
//        throw new NoExistException(
//                "The person with id " + identification + " does not exist into db, cannot be edited");
//    }
//
//
//    public Boolean drop(Integer identification) throws NoExistException {
//        Study oldStudy = studyPersistence.findById(identification);
//        if (Study != null)
//            return studyPersistence.delete(identification);
//        throw new NoExistException(
//                "The person with id " + identification + " does not exist into db, cannot be dropped");
//    }

//    public List<Study> findAll() {
//        log.info("Output: " + studyPersistence.getClass());
//        return studyPersistence.find();
//    }


//    public Study findOne(Integer identification) throws NoExistException {
//        Study oldStudy = studyPersistence.findById(identification);
//        if (Study != null)
//            return Study;
//        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
//    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getPerson(Integer identification) throws NoExistException {
        return null;
    }

    @Override
    public Study getProfesion(Integer identification) throws NoExistException {
        return null;
    }


    public List<Study> getStudies(Integer identification) throws NoExistException {
//        Study oldStudy = studyPersistence.findById(identification);
//        if (Study != null)
//            return Study.getStudies();
//        throw new NoExistException(
//                "The person with id " + identification + " does not exist into db, cannot be getting studies");
        return  null;
    }
}
