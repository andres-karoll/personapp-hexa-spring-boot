package co.edu.javeriana.as.personapp.mongo.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Mapper
public class ProfesionMapperMongo {

	public ProfesionDocument fromDomainToAdapter(Profession profession) {
		ProfesionDocument profesionDocument = new ProfesionDocument();
		profesionDocument.setId(profession.getIdentification());
		profesionDocument.setNom(profession.getName());
		profesionDocument.setDes(profession.getDescription() != null ? profession.getDescription() : "");
		return profesionDocument;
	}

	public Profession fromAdapterToDomain(ProfesionDocument profesionDocument) {
		Profession profession = new Profession(profesionDocument.getId(), profesionDocument.getNom());
		profession.setDescription(profesionDocument.getDes() != null ? profesionDocument.getDes() : "");
		return profession;
	}
}
