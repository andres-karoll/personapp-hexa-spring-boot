package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

	private final ProfessionOutputPort professionPersistence;

	public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
		this.professionPersistence = professionPersistence;
	}

	@Override
	public Profession create(Profession profession) throws AlreadyExistException {
		if (professionPersistence.findById(profession.getIdentification()) != null)
			throw new AlreadyExistException("The profession " + profession.getIdentification() + " already exists");
		return professionPersistence.save(profession);
	}

	@Override
	public Profession edit(Integer identification, Profession profession) throws NoExistException {
		if (professionPersistence.findById(identification) == null)
			throw new NoExistException(
					"The profession with id " + identification + " does not exist into db, cannot be edited");
		profession.setIdentification(identification);
		return professionPersistence.save(profession);
	}

	@Override
	public Boolean drop(Integer identification) throws NoExistException {
		if (professionPersistence.findById(identification) == null)
			throw new NoExistException(
					"The profession with id " + identification + " does not exist into db, cannot be dropped");
		return professionPersistence.delete(identification);
	}

	@Override
	public List<Profession> findAll() {
		return professionPersistence.find();
	}

	@Override
	public Profession findOne(Integer identification) throws NoExistException {
		Profession profession = professionPersistence.findById(identification);
		if (profession == null)
			throw new NoExistException(
					"The profession with id " + identification + " does not exist into db, cannot be found");
		return profession;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
