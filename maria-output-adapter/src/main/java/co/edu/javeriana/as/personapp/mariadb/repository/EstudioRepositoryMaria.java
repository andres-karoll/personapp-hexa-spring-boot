package co.edu.javeriana.as.personapp.mariadb.repository;

import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudioRepositoryMaria  extends JpaRepository<EstudiosEntity, EstudiosEntityPK> {
}
