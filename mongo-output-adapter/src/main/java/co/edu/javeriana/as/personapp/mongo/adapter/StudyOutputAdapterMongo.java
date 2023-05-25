package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudioRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudioRepositoryMongo estudioRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        try {
            EstudiosDocument persistedEstudio = estudioRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
            return estudiosMapperMongo.fromAdapterToDomain(persistedEstudio);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return study;
        }
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MongoDB");
        estudioRepositoryMongo.deleteById(identification);
        return estudioRepositoryMongo.findById(identification).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudioRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer identification) {
        log.debug("Into findById on Adapter MongoDB");
        if (estudioRepositoryMongo.findById(identification).isEmpty()) {
            return null;
        } else {
            return estudiosMapperMongo.fromAdapterToDomain(estudioRepositoryMongo.findById(identification).get());
        }
    }
}
