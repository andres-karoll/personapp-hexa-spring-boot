package co.edu.javeriana.as.personapp.mariadb.mapper;


import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TelefonoMariaMapper {
    @Mapping(target = "num", source = "phone.number")
    @Mapping(target = "oper", source = "phone.company")
    @Mapping(target = "duenio", source = "phone.owner")
    TelefonoEntity fromDomainToAdapter(Phone phone);

    @Mapping(target = "number", source = "telefonoEntity.num")
    @Mapping(target = "company", source = "telefonoEntity.oper")
    @Mapping(target = "owner", source = "telefonoEntity.duenio")
    Phone fromAdapterToDomain(TelefonoEntity telefonoEntity);
}
