package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	private PersistenceRegistry<ProfessionOutputPort> registry;

	private ProfessionInputPort useCase;

	public void setDatabase(String database) throws InvalidOptionException {
		useCase = new ProfessionUseCase(registry.resolve(database));
	}

	public List<Profession> historial() {
		return useCase.findAll();
	}

	public Profession buscar(Integer id) throws NoExistException {
		return useCase.findOne(id);
	}

	public Profession crear(Integer id, String nombre, String descripcion) throws AlreadyExistException {
		return useCase.create(build(id, nombre, descripcion));
	}

	public Profession editar(Integer id, String nombre, String descripcion) throws NoExistException {
		return useCase.edit(id, build(id, nombre, descripcion));
	}

	public Boolean eliminar(Integer id) throws NoExistException {
		return useCase.drop(id);
	}

	private Profession build(Integer id, String nombre, String descripcion) {
		Profession profession = new Profession(id, nombre);
		profession.setDescription(descripcion);
		return profession;
	}
}
