package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProfesionMapperMaria {

    @Mapping(target = "id", source = "profession.identification")
    @Mapping(target = "nom", source = "profession.name")
    @Mapping(target = "des", source = "profession.description")
    @Mapping(target = "estudios", source = "profession.studies")
    ProfesionEntity fromDomainToAdapter(Profession profession);

    @Mapping(target = "identification", source = "profesionEntity.id")
    @Mapping(target = "name", source = "profesionEntity.nom")
    @Mapping(target = "description", source = "profesionEntity.des")
    @Mapping(target = "studies", source = "profesionEntity.estudios")
    Profession fromAdapterToDomain(ProfesionEntity profesionEntity);

//	@Autowired
//	private EstudiosMapperMaria estudiosMapperMaria;
//
//	public ProfesionEntity fromDomainToAdapter(Profession profession) {
//		ProfesionEntity profesionEntity = new ProfesionEntity();
//		profesionEntity.setId(profession.getIdentification());
//		profesionEntity.setNom(profession.getName());
//		profesionEntity.setDes(validateDes(profession.getDescription()));
//		profesionEntity.setEstudios(validateEstudios(profession.getStudies()));
//		return profesionEntity;
//	}
//
//	private String validateDes(String description) {
//		return description != null ? description : "";
//	}
//
//	private List<EstudiosEntity> validateEstudios(List<Study> studies) {
//		return studies != null && !studies.isEmpty() ? studies.stream()
//				.map(study -> estudiosMapperMaria.fromDomainToAdapter(study)).collect(Collectors.toList())
//				: new ArrayList<EstudiosEntity>();
//	}
//
//	public Profession fromAdapterToDomain(ProfesionEntity profesionEntity) {
//		Profession profession = new Profession();
//		profession.setIdentification(profesionEntity.getId());
//		profession.setName(profesionEntity.getNom());
//		profession.setDescription(validateDescription(profesionEntity.getDes()));
//		profession.setStudies(validateStudies(profesionEntity.getEstudios()));
//		return profession;
//	}
//
//	private String validateDescription(String des) {
//		return des != null ? des : "";
//	}
//
//	private List<Study> validateStudies(List<EstudiosEntity> estudiosEntity) {
//		return estudiosEntity != null && !estudiosEntity.isEmpty() ? estudiosEntity.stream()
//				.map(estudio -> estudiosMapperMaria.fromAdapterToDomain(estudio, true)).collect(Collectors.toList())
//				: new ArrayList<Study>();
//	}
}
