package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionResponse {
	private String id;
	private String nombre;
	private String descripcion;
	private String database;
	private String status;
}
