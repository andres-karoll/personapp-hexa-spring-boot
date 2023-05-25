package co.edu.javeriana.as.personapp.mongo.repository;

import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TelefonoRepositoryMongo extends MongoRepository<TelefonoDocument, Integer> {
}
