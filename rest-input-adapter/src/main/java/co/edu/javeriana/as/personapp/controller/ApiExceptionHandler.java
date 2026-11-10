package co.edu.javeriana.as.personapp.controller;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(InvalidOptionException.class)
	public ResponseEntity<Response> invalidOption(InvalidOptionException e) {
		return build(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(NoExistException.class)
	public ResponseEntity<Response> notFound(NoExistException e) {
		return build(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<Response> alreadyExist(AlreadyExistException e) {
		return build(HttpStatus.CONFLICT, e.getMessage());
	}

	@ExceptionHandler({DataIntegrityViolationException.class, ObjectRetrievalFailureException.class})
	public ResponseEntity<Response> integrity(RuntimeException e) {
		log.warn(e.getMessage());
		return build(HttpStatus.CONFLICT,
				"Violación de integridad: llave duplicada o referencia inexistente (llave foránea)");
	}

	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, DateTimeException.class})
	public ResponseEntity<Response> invalidData(RuntimeException e) {
		log.warn("Invalid data in request", e);
		return build(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos: " + e.getMessage());
	}

	private ResponseEntity<Response> build(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new Response("ERROR", message, LocalDateTime.now()));
	}
}
