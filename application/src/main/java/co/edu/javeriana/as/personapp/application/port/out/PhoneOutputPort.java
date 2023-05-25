package co.edu.javeriana.as.personapp.application.port.out;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Phone;

import java.util.List;

@Port
public interface PhoneOutputPort {
    public Phone save(Phone person);
    public Boolean delete(Integer identification);
    public List<Phone> find();
    public Phone findById(Integer identification);
}
