package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {
    @Autowired
    private EstudiosRepositoryMaria estudiosRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MariaDB");
        EstudiosEntity persistedEstudios = estudiosRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study));
        return estudiosMapperMaria.fromAdapterToDomain(persistedEstudios);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MariaDB");
        estudiosRepositoryMaria.deleteById(identification);
        return estudiosRepositoryMaria.findById(identification).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MariaDB");
        return estudiosRepositoryMaria.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer identification) {
        log.debug("Into findById on Adapter MariaDB");
        if (estudiosRepositoryMaria.findById(identification).isEmpty()) {
            return null;
        } else {
            return estudiosMapperMaria.fromAdapterToDomain(estudiosRepositoryMaria.findById(identification).get());
        }
    }
}
