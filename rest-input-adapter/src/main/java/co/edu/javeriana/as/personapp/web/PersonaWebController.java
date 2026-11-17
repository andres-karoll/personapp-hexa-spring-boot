package co.edu.javeriana.as.personapp.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Controller
@RequestMapping("/web/personas")
public class PersonaWebController {

	private static final String BASE = "/web/personas";

	@Autowired
	private PersonaInputAdapterRest adapter;

	@GetMapping
	public String listado(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		String motor = WebSupport.motor(db);
		model.addAttribute("db", motor);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
		try {
			model.addAttribute("personas", adapter.historial(motor));
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			model.addAttribute("personas", List.of());
		}
		return "persona/listado";
	}

	@GetMapping("/nueva")
	public String formularioCrear(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		PersonaRequest persona = new PersonaRequest();
		persona.setDatabase(WebSupport.motor(db));
		return formulario(model, persona, BASE, "Nueva persona", false);
	}

	@PostMapping
	public String crear(@ModelAttribute("persona") PersonaRequest persona, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(persona.getDatabase());
		try {
			adapter.crearPersona(persona);
			redirect.addFlashAttribute("mensaje", "Persona creada.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, persona, BASE, "Nueva persona", false);
		}
	}

	@GetMapping("/{db}/{id}/editar")
	public String formularioEditar(@PathVariable String db, @PathVariable Integer id, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			PersonaResponse actual = adapter.buscar(motor, id);
			actual.setDatabase(motor);
			return formulario(model, actual, BASE + "/" + motor + "/" + id, "Editar persona", true);
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{id}")
	public String editar(@PathVariable String db, @PathVariable Integer id,
			@ModelAttribute("persona") PersonaRequest persona, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		persona.setDatabase(motor);
		try {
			adapter.editar(motor, id, persona);
			redirect.addFlashAttribute("mensaje", "Persona actualizada.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, persona, BASE + "/" + motor + "/" + id, "Editar persona", true);
		}
	}

	@GetMapping("/{db}/{id}/eliminar")
	public String confirmarEliminar(@PathVariable String db, @PathVariable Integer id, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			model.addAttribute("persona", adapter.buscar(motor, id));
			model.addAttribute("db", motor);
			model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
			model.addAttribute("id", id);
			return "persona/confirmar";
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{id}/eliminar")
	public String eliminar(@PathVariable String db, @PathVariable Integer id, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			adapter.eliminar(motor, id);
			redirect.addFlashAttribute("mensaje", "Persona eliminada, junto con sus teléfonos y estudios.");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
		}
		return "redirect:" + BASE + "?db=" + motor;
	}

	private String formulario(Model model, PersonaRequest persona, String accion, String titulo, boolean editando) {
		model.addAttribute("persona", persona);
		model.addAttribute("accion", accion);
		model.addAttribute("titulo", titulo);
		model.addAttribute("editando", editando);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(persona.getDatabase()));
		return "persona/formulario";
	}
}
