package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EstudiosMariaMapper {

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
}
