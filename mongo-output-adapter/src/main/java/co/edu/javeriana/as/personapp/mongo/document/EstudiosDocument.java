package co.edu.javeriana.as.personapp.mongo.document;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("estudios")
public class EstudiosDocument {
	@Id
	private String id;
	@DocumentReference(lazy = true)
	private PersonaDocument primaryPersona;
	@DocumentReference(lazy = true)
	private ProfesionDocument primaryProfesion;
	private LocalDate fecha;
	private String univer;
}
