package co.edu.javeriana.as.personapp.mariadb.repository;

import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefonoRepositoryMaria extends JpaRepository<TelefonoEntity, String> {
}
