package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@Adapter
public class EstudioInputAdapterCli {

	@Autowired
	private PersistenceRegistry<StudyOutputPort> registry;

	private StudyInputPort useCase;

	public void setDatabase(String database) throws InvalidOptionException {
		useCase = new StudyUseCase(registry.resolve(database));
	}

	public List<Study> historial() {
		return useCase.findAll();
	}

	public Study buscar(Integer dniPersona, Integer idProfesion) throws NoExistException {
		return useCase.findOne(dniPersona, idProfesion);
	}

	public Study crear(Integer dniPersona, Integer idProfesion, LocalDate fecha, String universidad)
			throws AlreadyExistException {
		return useCase.create(build(dniPersona, idProfesion, fecha, universidad));
	}

	public Study editar(Integer dniPersona, Integer idProfesion, LocalDate fecha, String universidad)
			throws NoExistException {
		return useCase.edit(dniPersona, idProfesion, build(dniPersona, idProfesion, fecha, universidad));
	}

	public Boolean eliminar(Integer dniPersona, Integer idProfesion) throws NoExistException {
		return useCase.drop(dniPersona, idProfesion);
	}

	private Study build(Integer dniPersona, Integer idProfesion, LocalDate fecha, String universidad) {
		Person person = new Person();
		person.setIdentification(dniPersona);
		Study study = new Study(person, new Profession(idProfesion, ""));
		study.setGraduationDate(fecha);
		study.setUniversityName(universidad);
		return study;
	}
}
