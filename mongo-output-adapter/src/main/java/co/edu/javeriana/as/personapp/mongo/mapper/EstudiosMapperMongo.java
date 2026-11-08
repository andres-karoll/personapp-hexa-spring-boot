package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Mapper
public class EstudiosMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	@Autowired
	private ProfesionMapperMongo profesionMapperMongo;

	public static String buildId(Integer personIdentification, Integer professionIdentification) {
		return personIdentification + "-" + professionIdentification;
	}

	public EstudiosDocument fromDomainToAdapter(Study study) {
		EstudiosDocument estudio = new EstudiosDocument();
		estudio.setId(buildId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
		PersonaDocument persona = new PersonaDocument();
		persona.setId(study.getPerson().getIdentification());
		estudio.setPrimaryPersona(persona);
		ProfesionDocument profesion = new ProfesionDocument();
		profesion.setId(study.getProfession().getIdentification());
		estudio.setPrimaryProfesion(profesion);
		estudio.setFecha(study.getGraduationDate());
		estudio.setUniver(study.getUniversityName() != null ? study.getUniversityName() : "");
		return estudio;
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study(
				personaMapperMongo.fromAdapterToDomainShallow(estudiosDocument.getPrimaryPersona()),
				profesionMapperMongo.fromAdapterToDomain(estudiosDocument.getPrimaryProfesion()));
		study.setGraduationDate(estudiosDocument.getFecha());
		study.setUniversityName(estudiosDocument.getUniver() != null ? estudiosDocument.getUniver() : "");
		return study;
	}
}
