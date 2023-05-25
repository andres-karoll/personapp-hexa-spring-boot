package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersintence;

    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersintence) {
        this.phonePersintence=phonePersintence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersintence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Into create on Application Domain");
        return phonePersintence.save(phone);
    }

    @Override
    public Phone edit(Integer identification, Phone phone) throws NoExistException {
        Phone oldPerson = phonePersintence.findById(identification);
        if (oldPerson != null)
            return phonePersintence.save(phone);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Phone oldPhone = phonePersintence.findById(identification);
        if (oldPhone != null)
            return phonePersintence.delete(identification);
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Phone> findAll() {
        log.info("Output: " + phonePersintence.getClass());
        return phonePersintence.find();
    }

    @Override
    public Phone findOne(Integer identification) throws NoExistException {
        Phone oldPhone = phonePersintence.findById(identification);
        if (oldPhone != null)
            return oldPhone;
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getPerson(Integer identification) throws NoExistException {
        Phone phone = phonePersintence.findById(identification);
        if (phone != null)
            return phone.getOwner();
        throw new NoExistException("The person with id " + identification + " does not exist into db, cannot be found");
    }
}
