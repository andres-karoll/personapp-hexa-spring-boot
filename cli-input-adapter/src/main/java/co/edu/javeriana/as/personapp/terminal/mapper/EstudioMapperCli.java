package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

import java.sql.Date;
import java.time.ZoneId;

@Mapper
public class EstudioMapperCli {
    public EstudiosModelCli fromDomainToAdapterCli(Study study) {
        EstudiosModelCli estudiosModelCli = new EstudiosModelCli();
        estudiosModelCli.setId_prof(study.getProfession().getIdentification());
        estudiosModelCli.setCc_per(study.getPerson().getIdentification());
        estudiosModelCli.setFecha(Date.from(study.getGraduationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        estudiosModelCli.setUniver(study.getUniversityName());
        return estudiosModelCli;
    }
}
