package co.edu.javeriana.as.personapp.mariadb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;

public interface PersonaRepositoryMaria extends JpaRepository<PersonaEntity, Integer>{

}
