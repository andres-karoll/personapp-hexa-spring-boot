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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Controller
@RequestMapping("/web/profesiones")
public class ProfesionWebController {

	private static final String BASE = "/web/profesiones";

	@Autowired
	private ProfesionInputAdapterRest adapter;

	@GetMapping
	public String listado(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		String motor = WebSupport.motor(db);
		model.addAttribute("db", motor);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
		try {
			model.addAttribute("profesiones", adapter.historial(motor));
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			model.addAttribute("profesiones", List.of());
		}
		return "profesion/listado";
	}

	@GetMapping("/nueva")
	public String formularioCrear(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		ProfesionRequest profesion = new ProfesionRequest();
		profesion.setDatabase(WebSupport.motor(db));
		return formulario(model, profesion, BASE, "Nueva profesión", false);
	}

	@PostMapping
	public String crear(@ModelAttribute("profesion") ProfesionRequest profesion, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(profesion.getDatabase());
		try {
			adapter.crear(profesion);
			redirect.addFlashAttribute("mensaje", "Profesión creada.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, profesion, BASE, "Nueva profesión", false);
		}
	}

	@GetMapping("/{db}/{id}/editar")
	public String formularioEditar(@PathVariable String db, @PathVariable Integer id, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			ProfesionResponse actual = adapter.buscar(motor, id);
			ProfesionRequest profesion = new ProfesionRequest(actual.getId(), actual.getNombre(), actual.getDescripcion(), motor);
			return formulario(model, profesion, BASE + "/" + motor + "/" + id, "Editar profesión", true);
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{id}")
	public String editar(@PathVariable String db, @PathVariable Integer id,
			@ModelAttribute("profesion") ProfesionRequest profesion, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		profesion.setDatabase(motor);
		try {
			adapter.editar(motor, id, profesion);
			redirect.addFlashAttribute("mensaje", "Profesión actualizada.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, profesion, BASE + "/" + motor + "/" + id, "Editar profesión", true);
		}
	}

	@GetMapping("/{db}/{id}/eliminar")
	public String confirmarEliminar(@PathVariable String db, @PathVariable Integer id, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			model.addAttribute("profesion", adapter.buscar(motor, id));
			model.addAttribute("db", motor);
			model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
			model.addAttribute("id", id);
			return "profesion/confirmar";
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
			redirect.addFlashAttribute("mensaje", "Profesión eliminada.");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
		}
		return "redirect:" + BASE + "?db=" + motor;
	}

	private String formulario(Model model, ProfesionRequest profesion, String accion, String titulo, boolean editando) {
		model.addAttribute("profesion", profesion);
		model.addAttribute("accion", accion);
		model.addAttribute("titulo", titulo);
		model.addAttribute("editando", editando);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(profesion.getDatabase()));
		return "profesion/formulario";
	}
}
