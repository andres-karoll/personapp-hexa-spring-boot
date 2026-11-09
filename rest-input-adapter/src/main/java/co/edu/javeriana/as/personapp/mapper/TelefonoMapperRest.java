package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.common.validation.Entrada;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {

	public Phone fromAdapterToDomain(String numero, TelefonoRequest request) {
		Person owner = new Person();
		owner.setIdentification(Entrada.positivo("duenio", Entrada.entero("duenio", request.getDuenio())));
		return new Phone(Entrada.texto("numero", numero, Entrada.MAX_NUMERO_TELEFONO),
				Entrada.texto("operador", request.getOperador(), Entrada.MAX_OPERADOR), owner);
	}

	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		return new TelefonoResponse(phone.getNumber(), phone.getCompany(),
				phone.getOwner().getIdentification() + "", DatabaseOption.displayNameOf(database), "OK");
	}
}
