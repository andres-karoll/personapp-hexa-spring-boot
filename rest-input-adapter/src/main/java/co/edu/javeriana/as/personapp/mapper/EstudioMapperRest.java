package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.common.validation.Entrada;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {

	public Study fromAdapterToDomain(Integer dniPersona, Integer idProfesion, EstudioRequest request) {
		Person person = new Person();
		person.setIdentification(Entrada.positivo("dniPersona", dniPersona));
		Profession profession = new Profession(Entrada.positivo("idProfesion", idProfesion), "");
		Study study = new Study(person, profession);
		study.setGraduationDate(Entrada.fechaOpcional("fecha", request.getFecha()));
		study.setUniversityName(Entrada.textoOpcional("universidad", request.getUniversidad(), Entrada.MAX_UNIVERSIDAD));
		return study;
	}

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		return new EstudioResponse(study.getPerson().getIdentification() + "",
				study.getProfession().getIdentification() + "",
				study.getGraduationDate() != null ? study.getGraduationDate().toString() : "",
				study.getUniversityName(), DatabaseOption.displayNameOf(database), "OK");
	}
}
