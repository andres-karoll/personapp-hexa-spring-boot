package co.edu.javeriana.as.personapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

public interface TelefonoRepositoryMongo extends MongoRepository<TelefonoDocument, String> {

	/** Borrado en cascada: elimina los teléfonos cuyo dueño es la persona indicada. */
	@Query(value = "{ 'primaryDuenio' : ?0 }", delete = true)
	void deleteByOwner(Integer personIdentification);
}
