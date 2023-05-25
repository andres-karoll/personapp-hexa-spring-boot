package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {
    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on Adapter MariaDB");
        TelefonoEntity persistedPhone = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone));
        return telefonoMapperMaria.fromAdapterToDomain(persistedPhone);
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MariaDB");
        telefonoRepositoryMaria.deleteById(identification);
        return telefonoRepositoryMaria.findById(identification).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Into find on Adapter MariaDB");
        return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(Integer identification) {
        log.debug("Into findById on Adapter MariaDB");
        if (telefonoRepositoryMaria.findById(identification).isEmpty()) {
            return null;
        } else {
            return telefonoMapperMaria.fromAdapterToDomain(telefonoRepositoryMaria.findById(identification).get());
        }
    }
}
