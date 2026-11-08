package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {
	private String dniPersona;
	private String idProfesion;
	private String fecha;
	private String universidad;
	private String database;
}
