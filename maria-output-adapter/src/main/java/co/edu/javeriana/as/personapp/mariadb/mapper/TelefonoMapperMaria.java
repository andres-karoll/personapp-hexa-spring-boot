package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.domain.Gender;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import org.mapstruct.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface TelefonoMapperMaria {

    @Mapping(target = "num", source = "phone.number")
    @Mapping(target = "oper", source = "phone.company")
    @Mapping(target = "duenio", source = "phone.owner")
    TelefonoEntity fromDomainToAdapter(Phone phone);

    @Mapping(target = "number", source = "telefonoEntity.num")
    @Mapping(target = "company", source = "telefonoEntity.oper")
    @Mapping(target = "owner", source = "telefonoEntity.duenio", qualifiedByName = "duenioToOwner")
    Phone fromAdapterToDomain(TelefonoEntity telefonoEntity);

    @Named("duenioToOwner")
    default Person telefonoToPhone(PersonaEntity personaEntity) {
        return new Person(
                personaEntity.getCc(),
                personaEntity.getNombre(),
                personaEntity.getApellido(),
                generoToGender(personaEntity.getGenero()),
                personaEntity.getEdad());
    }


    @Named("generoToGender")
    default Gender generoToGender(Character genero) {
        return genero == 'F' ? Gender.FEMALE : genero == 'M' ? Gender.MALE : Gender.OTHER;
    }
}