package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {
    public TelefonoModelCli fromDomainToAdapterCli(Phone telefono) {
        TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
        telefonoModelCli.setNum(telefono.getNumber());
        telefonoModelCli.setOper(telefono.getCompany());
        telefonoModelCli.setDuenio(telefono.getOwner());
        return telefonoModelCli;
    }
}
