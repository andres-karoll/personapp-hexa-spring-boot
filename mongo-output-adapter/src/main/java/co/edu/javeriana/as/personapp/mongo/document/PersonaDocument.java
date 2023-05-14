package co.edu.javeriana.as.personapp.mongo.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("persona")
public class PersonaDocument {
	@Id
	private Integer id;
	private String nombre;
	private String apellido;
	private String genero;
	private Integer edad;
	@DocumentReference(lazy = true, lookup = "{ 'primaryDuenio' : ?#{#self._id} }")
	@ReadOnlyProperty
	private List<TelefonoDocument> telefonos;
	@DocumentReference(lazy = true, lookup = "{ 'primaryPersona' : ?#{#self._id} }")
	@ReadOnlyProperty
	private List<EstudiosDocument> estudios;
}
