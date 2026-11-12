package co.edu.javeriana.as.personapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;

public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {
    @org.springframework.data.mongodb.repository.Query(value = "{ 'primaryProfesion': ?0 }", exists = true)
    boolean existsByProfessionId(Integer professionId);
    @org.springframework.data.mongodb.repository.Query(value = "{ 'primaryPersona': ?0 }", exists = true)
    boolean existsByPersonId(Integer personId);
}
