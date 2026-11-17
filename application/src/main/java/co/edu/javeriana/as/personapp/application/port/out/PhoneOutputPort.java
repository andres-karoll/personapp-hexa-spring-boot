package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Phone;

@Port
public interface PhoneOutputPort {
	public Phone save(Phone phone);
	public Boolean delete(String number);
	public List<Phone> find();
	public Phone findById(String number);
}
