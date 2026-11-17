package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

	@Autowired
	private EstudioInputAdapterRest adapter;

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EstudioResponse> listar(@PathVariable String database) throws InvalidOptionException {
		return adapter.historial(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/{dniPersona}/{idProfesion}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse buscar(@PathVariable String database, @PathVariable Integer dniPersona,
			@PathVariable Integer idProfesion) throws InvalidOptionException, NoExistException {
		return adapter.buscar(database.toUpperCase(), dniPersona, idProfesion);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse crear(@RequestBody EstudioRequest request) throws InvalidOptionException, AlreadyExistException {
		return adapter.crear(request);
	}

	@PutMapping(path = "/{database}/{dniPersona}/{idProfesion}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse editar(@PathVariable String database, @PathVariable Integer dniPersona,
			@PathVariable Integer idProfesion, @RequestBody EstudioRequest request)
			throws InvalidOptionException, NoExistException {
		return adapter.editar(database.toUpperCase(), dniPersona, idProfesion, request);
	}

	@DeleteMapping(path = "/{database}/{dniPersona}/{idProfesion}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable String database, @PathVariable Integer dniPersona,
			@PathVariable Integer idProfesion) throws InvalidOptionException, NoExistException {
		adapter.eliminar(database.toUpperCase(), dniPersona, idProfesion);
	}
}
