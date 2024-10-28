package co.edu.javeriana.as.personapp.mariadb.adapter;


import java.util.List;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Adapter("telefonoOutputAdapterMaria")
@Transactional
public class TelefonoOutputAdapterMaria implements PhoneOutputPort {
    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone telefono) {
        //log.debug("Into save on Adapter MariaDB");

        TelefonoEntity persistedTelefono = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(telefono));
        return telefonoMapperMaria.fromAdapterToDomain(persistedTelefono);
    }

    @Override
    public Boolean delete(String num) {
        //log.debug("Into delete on Adapter MariaDB");

        telefonoRepositoryMaria.deleteById(num);
        return telefonoRepositoryMaria.findById(num).isEmpty();
    }

    @Override
    public List<Phone> find() {
        //log.debug("Into find on Adapter MariaDB");

        return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String num) {
        //log.debug("Into findById on Adapter MariaDB");

        if (telefonoRepositoryMaria.findById(num).isEmpty()) {
            return null;
        } else {
            return telefonoMapperMaria.fromAdapterToDomain(telefonoRepositoryMaria.findById(num).get());
        }
    }
}
