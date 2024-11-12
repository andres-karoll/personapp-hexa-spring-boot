package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PersonaMapperMaria {
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
        return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : 'F';
    }


//	@Autowired
//	private EstudiosMapperMaria estudiosMapperMaria;
//
//	@Autowired
//	private TelefonoMapperMaria telefonoMapperMaria;
//
//	public PersonaEntity fromDomainToAdapter(Person person) {
//		PersonaEntity personaEntity = new PersonaEntity();
//		personaEntity.setCc(person.getIdentification());
//		personaEntity.setNombre(person.getFirstName());
//		personaEntity.setApellido(person.getLastName());
//		personaEntity.setGenero(validateGenero(person.getGender()));
//		personaEntity.setEdad(validateEdad(person.getAge()));
//		personaEntity.setEstudios(validateEstudios(person.getStudies()));
//		personaEntity.setTelefonos(validateTelefonos(person.getPhoneNumbers()));
//		return personaEntity;
//	}
//
//	private Character validateGenero(@NonNull Gender gender) {
//		return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : ' ';
//	}
//
//	private Integer validateEdad(Integer age) {
//		return age != null && age >= 0 ? age : null;
//	}
//
//	private List<EstudiosEntity> validateEstudios(List<Study> studies) {
//		return studies != null && !studies.isEmpty()
//				? studies.stream().map(study -> estudiosMapperMaria.fromDomainToAdapter(study)).collect(Collectors.toList())
//				: new ArrayList<EstudiosEntity>();
//	}
//
//	private List<TelefonoEntity> validateTelefonos(List<Phone> phoneNumbers) {
//		return phoneNumbers != null && !phoneNumbers.isEmpty() ? phoneNumbers.stream()
//				.map(phone -> telefonoMapperMaria.fromDomainToAdapter(phone)).collect(Collectors.toList())
//				: new ArrayList<TelefonoEntity>();
//	}
//
//	public Person fromAdapterToDomain(PersonaEntity personaEntity, boolean firstReference) {
//		Person person = new Person();
}