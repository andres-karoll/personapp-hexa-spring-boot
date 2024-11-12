package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudioRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {
    @Autowired
    private EstudioRepositoryMongo estudiosRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study phone) {
        log.debug("Into save on Adapter MongoDB");
        try {
            EstudiosDocument persistedTelefono = estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(phone));
            return estudiosMapperMongo.fromAdapterToDomain(persistedTelefono);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return phone;
        }
    }


    public Boolean delete(String num) {
        log.debug("Into delete on Adapter MongoDB");
        estudiosRepositoryMongo.deleteById(num);
        return estudiosRepositoryMongo.findById(num).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudiosRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    public Study findById(String num) {
        log.debug("Into findById on Adapter MongoDB");
        if (estudiosRepositoryMongo.findById(num).isEmpty()) {
            return null;
        } else {
            return estudiosMapperMongo.fromAdapterToDomain(estudiosRepositoryMongo.findById(num).get());
        }
    }
}
