package co.edu.javeriana.as.personapp.terminal.model;

import co.edu.javeriana.as.personapp.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoModelCli {
    private String num;
    private String oper;
    private Person duenio;

}
