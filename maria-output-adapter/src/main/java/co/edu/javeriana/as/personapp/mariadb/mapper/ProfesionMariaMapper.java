package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProfesionMariaMapper {
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
}
