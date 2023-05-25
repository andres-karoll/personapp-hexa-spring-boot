package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistance) {
        this.professionPersistence = professionPersistance;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Into create on Application Domain");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer identification, Profession profession) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if (oldProfession != null)
            return professionPersistence.save(profession);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if (oldProfession != null)
            return professionPersistence.delete(identification);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Profession> findAll() {
        log.info("Output: " + professionPersistence.getClass());
        return professionPersistence.find();
    }

    @Override
    public Profession findOne(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if (oldProfession != null)
            return oldProfession;
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if (oldProfession != null)
            return oldProfession.getStudies();
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be getting studies");
    }
}
