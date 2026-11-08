package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyOutputPort {
	public Study save(Study study);
	public Boolean delete(Integer personIdentification, Integer professionIdentification);
	public List<Study> find();
	public Study findById(Integer personIdentification, Integer professionIdentification);
}
