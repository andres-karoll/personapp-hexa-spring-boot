package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

@UseCase
public class PhoneUseCase implements PhoneInputPort {

	private final PhoneOutputPort phonePersistence;

	public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
		this.phonePersistence = phonePersistence;
	}

	@Override
	public Phone create(Phone phone) throws AlreadyExistException {
		if (phonePersistence.findById(phone.getNumber()) != null)
			throw new AlreadyExistException("The phone " + phone.getNumber() + " already exists");
		return phonePersistence.save(phone);
	}

	@Override
	public Phone edit(String number, Phone phone) throws NoExistException {
		if (phonePersistence.findById(number) == null)
			throw new NoExistException("The phone " + number + " does not exist into db, cannot be edited");
		phone.setNumber(number);
		return phonePersistence.save(phone);
	}

	@Override
	public Boolean drop(String number) throws NoExistException {
		if (phonePersistence.findById(number) == null)
			throw new NoExistException("The phone " + number + " does not exist into db, cannot be dropped");
		return phonePersistence.delete(number);
	}

	@Override
	public List<Phone> findAll() {
		return phonePersistence.find();
	}

	@Override
	public Phone findOne(String number) throws NoExistException {
		Phone phone = phonePersistence.findById(number);
		if (phone == null)
			throw new NoExistException("The phone " + number + " does not exist into db, cannot be found");
		return phone;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
