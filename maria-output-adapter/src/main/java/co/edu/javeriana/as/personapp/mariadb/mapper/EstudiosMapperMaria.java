package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface EstudiosMapperMaria {

    @Mapping(target = "persona", source = "study.person")
    @Mapping(target = "profesion", source = "study.profession")
    @Mapping(target = "univer", source = "study.universityName")
    @Mapping(target = "fecha", source = "study.graduationDate")
    EstudiosEntity fromDomainToAdapter(Study study);

    @Mapping(target = "person", source = "estudiosEntity.persona")
    @Mapping(target = "profession", source = "estudiosEntity.profesion")
    @Mapping(target = "universityName", source = "estudiosEntity.univer")
    @Mapping(target = "graduationDate", source = "estudiosEntity.fecha")
    Study fromAdapterToDomain(EstudiosEntity estudiosEntity);

//	@Autowired
//	private PersonaMapperMaria personaMapperMaria;
//
//	@Autowired
//	private ProfesionMapperMaria profesionMapperMaria;
//
//	public EstudiosEntity fromDomainToAdapter(Study study) {
//		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
//		estudioPK.setCcPer(study.getPerson().getIdentification());
//		estudioPK.setIdProf(study.getProfession().getIdentification());
//		EstudiosEntity estudio = new EstudiosEntity();
//		estudio.setEstudiosPK(estudioPK);
//		estudio.setFecha(validateFecha(study.getGraduationDate()));
//		estudio.setUniver(validateUniver(study.getUniversityName()));
//		return estudio;
//	}
//
//	private Date validateFecha(LocalDate graduationDate) {
//		return graduationDate != null
//				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
//				: null;
//	}
//
//	private String validateUniver(String universityName) {
//		return universityName != null ? universityName : "";
//	}
//
//	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity, boolean firsReference) {
//		Study study = new Study();
//		if(firsReference){
//			study.setPerson(personaMapperMaria.fromAdapterToDomain(estudiosEntity.getPersona(), false));
//		}else{
//			study.setPerson(null);
//		}
//		study.setProfession(profesionMapperMaria.fromAdapterToDomain(estudiosEntity.getProfesion()));
//		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
//		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
//		return null;
//	}
//
//	private LocalDate validateGraduationDate(Date fecha) {
//		return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
//	}
//
//	private String validateUniversityName(String univer) {
//		return univer != null ? univer : "";
//	}
}