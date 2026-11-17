package co.edu.javeriana.as.personapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioWebController {

	@GetMapping("/")
	public String inicio() {
		return "inicio";
	}
}
