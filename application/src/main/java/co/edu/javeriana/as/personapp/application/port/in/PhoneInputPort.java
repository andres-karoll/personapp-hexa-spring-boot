package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

@Port
public interface PhoneInputPort {

	public Phone create(Phone phone) throws AlreadyExistException;

	public Phone edit(String number, Phone phone) throws NoExistException;

	public Boolean drop(String number) throws NoExistException;

	public List<Phone> findAll();

	public Phone findOne(String number) throws NoExistException;

	public Integer count();
}
