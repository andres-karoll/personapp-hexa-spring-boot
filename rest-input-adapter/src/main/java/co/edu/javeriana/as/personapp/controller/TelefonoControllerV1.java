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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {

	@Autowired
	private TelefonoInputAdapterRest adapter;

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TelefonoResponse> listar(@PathVariable String database) throws InvalidOptionException {
		return adapter.historial(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse buscar(@PathVariable String database, @PathVariable String numero)
			throws InvalidOptionException, NoExistException {
		return adapter.buscar(database.toUpperCase(), numero);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse crear(@RequestBody TelefonoRequest request) throws InvalidOptionException, AlreadyExistException {
		return adapter.crear(request);
	}

	@PutMapping(path = "/{database}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse editar(@PathVariable String database, @PathVariable String numero,
			@RequestBody TelefonoRequest request) throws InvalidOptionException, NoExistException {
		return adapter.editar(database.toUpperCase(), numero, request);
	}

	@DeleteMapping(path = "/{database}/{numero}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable String database, @PathVariable String numero)
			throws InvalidOptionException, NoExistException {
		adapter.eliminar(database.toUpperCase(), numero);
	}
}
