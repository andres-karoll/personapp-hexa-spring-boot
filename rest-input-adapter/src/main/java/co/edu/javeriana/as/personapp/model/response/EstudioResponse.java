package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioResponse {
	private String dniPersona;
	private String idProfesion;
	private String fecha;
	private String universidad;
	private String database;
	private String status;
}
