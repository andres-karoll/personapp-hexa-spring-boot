package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

@Mapper
public class ProfesionMapperMaria {

	public ProfesionEntity fromDomainToAdapter(Profession profession) {
		ProfesionEntity profesionEntity = new ProfesionEntity();
		profesionEntity.setId(profession.getIdentification());
		profesionEntity.setNom(profession.getName());
		profesionEntity.setDes(profession.getDescription() != null ? profession.getDescription() : "");
		return profesionEntity;
	}

	public Profession fromAdapterToDomain(ProfesionEntity profesionEntity) {
		Profession profession = new Profession(profesionEntity.getId(), profesionEntity.getNom());
		profession.setDescription(profesionEntity.getDes() != null ? profesionEntity.getDes() : "");
		return profession;
	}
}
