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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest adapter;

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> listar(@PathVariable String database) throws InvalidOptionException {
		return adapter.historial(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse buscar(@PathVariable String database, @PathVariable Integer id)
			throws InvalidOptionException, NoExistException {
		return adapter.buscar(database.toUpperCase(), id);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse crear(@RequestBody ProfesionRequest request) throws InvalidOptionException, AlreadyExistException {
		return adapter.crear(request);
	}

	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse editar(@PathVariable String database, @PathVariable Integer id,
			@RequestBody ProfesionRequest request) throws InvalidOptionException, NoExistException {
		return adapter.editar(database.toUpperCase(), id, request);
	}

	@DeleteMapping(path = "/{database}/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable String database, @PathVariable Integer id)
			throws InvalidOptionException, NoExistException {
		adapter.eliminar(database.toUpperCase(), id);
	}
}
