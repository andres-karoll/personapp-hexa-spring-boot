package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {

	public Study create(Study study) throws AlreadyExistException;

	public Study edit(Integer personIdentification, Integer professionIdentification, Study study)
			throws NoExistException;

	public Boolean drop(Integer personIdentification, Integer professionIdentification) throws NoExistException;

	public List<Study> findAll();

	public Study findOne(Integer personIdentification, Integer professionIdentification) throws NoExistException;

	public Integer count();
}
