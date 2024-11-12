package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface PersonaMariaMapper {

    @Mapping(target = "cc", source = "person.identification")
    @Mapping(target = "nombre", source = "person.firstName")
    @Mapping(target = "apellido", source = "person.lastName")
    @Mapping(target = "genero", source = "person.gender", qualifiedByName = "genderToGenero")
    @Mapping(target = "edad", source = "person.age")
    @Mapping(target = "telefonos", source = "person.phoneNumbers")
    @Mapping(target = "estudios", source = "person.studies")
    PersonaEntity fromDomainToAdapter(Person person);

    @Mapping(target = "identification", source = "personaEntity.cc")
    @Mapping(target = "firstName", source = "personaEntity.nombre")
    @Mapping(target = "lastName", source = "personaEntity.apellido")
    @Mapping(target = "gender", source = "personaEntity.genero", qualifiedByName = "generoToGender")
    @Mapping(target = "age", source = "personaEntity.edad")
    @Mapping(target = "phoneNumbers", source = "personaEntity.telefonos")
    @Mapping(target = "studies", source = "personaEntity.estudios")
    Person fromAdapterToDomain(PersonaEntity personaEntity);

    @Named("generoToGender")
    default Gender generoToGender(Character genero){
        return genero ==  'F'? Gender.FEMALE : genero == 'M' ? Gender.MALE   : Gender.OTHER;
    }

    @Named("genderToGenero")
    default Character generoToGender(Gender gender){
        return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : ' ';
    }
}
