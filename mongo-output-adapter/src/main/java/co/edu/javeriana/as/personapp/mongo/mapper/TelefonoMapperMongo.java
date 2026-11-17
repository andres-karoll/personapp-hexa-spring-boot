package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Mapper
public class TelefonoMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		PersonaDocument duenio = new PersonaDocument();
		duenio.setId(phone.getOwner().getIdentification());
		telefonoDocument.setPrimaryDuenio(duenio);
		return telefonoDocument;
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		return new Phone(telefonoDocument.getId(), telefonoDocument.getOper(),
				personaMapperMongo.fromAdapterToDomainShallow(telefonoDocument.getPrimaryDuenio()));
	}
}
