package co.edu.javeriana.as.personapp.application.port.in;


import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

import java.util.List;

@Port
public interface StudyInputPort {

    public void setPersintence(StudyOutputPort professionPersintence);

    public Study create(Study profession);

    public Study edit(Integer identification, Study profession) throws NoExistException;

    public Boolean drop(Integer identification) throws NoExistException;

    public List<Study> findAll();

    public Study findOne(Integer identification) throws NoExistException;

    public Integer count();


    public Person getPerson(Integer identification) throws NoExistException;
    public Study getProfesion(Integer identification) throws NoExistException;
}
