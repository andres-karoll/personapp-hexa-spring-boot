package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudiosModelCli {
    private Integer id_prof;
    private Integer cc_per;
    private Date fecha;
    private String univer;


}
