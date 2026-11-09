package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.common.validation.Entrada;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {

	public Profession fromAdapterToDomain(Integer id, ProfesionRequest request) {
		Profession profession = new Profession(Entrada.positivo("id", id),
				Entrada.texto("nombre", request.getNombre(), Entrada.MAX_NOMBRE_PROFESION));
		profession.setDescription(request.getDescripcion());
		return profession;
	}

	public ProfesionResponse fromDomainToAdapterRest(Profession profession, String database) {
		return new ProfesionResponse(profession.getIdentification() + "", profession.getName(),
				profession.getDescription(), DatabaseOption.displayNameOf(database), "OK");
	}
}
