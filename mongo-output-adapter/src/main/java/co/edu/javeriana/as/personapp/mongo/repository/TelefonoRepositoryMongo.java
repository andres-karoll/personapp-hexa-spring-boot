package co.edu.javeriana.as.personapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

public interface TelefonoRepositoryMongo extends MongoRepository<TelefonoDocument, String> {
    @org.springframework.data.mongodb.repository.Query(value = "{ 'primaryDuenio': ?0 }", exists = true)
    boolean existsByOwnerId(Integer personId);
}
