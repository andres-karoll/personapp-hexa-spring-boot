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

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Controller
@RequestMapping("/web/estudios")
public class EstudioWebController {

	private static final String BASE = "/web/estudios";

	@Autowired
	private EstudioInputAdapterRest adapter;

	@GetMapping
	public String listado(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		String motor = WebSupport.motor(db);
		model.addAttribute("db", motor);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
		try {
			model.addAttribute("estudios", adapter.historial(motor));
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			model.addAttribute("estudios", List.of());
		}
		return "estudio/listado";
	}

	@GetMapping("/nuevo")
	public String formularioCrear(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		EstudioRequest estudio = new EstudioRequest();
		estudio.setDatabase(WebSupport.motor(db));
		return formulario(model, estudio, BASE, "Nuevo estudio", false);
	}

	@PostMapping
	public String crear(@ModelAttribute("estudio") EstudioRequest estudio, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(estudio.getDatabase());
		try {
			adapter.crear(estudio);
			redirect.addFlashAttribute("mensaje", "Estudio creado.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, estudio, BASE, "Nuevo estudio", false);
		}
	}

	@GetMapping("/{db}/{dni}/{profesion}/editar")
	public String formularioEditar(@PathVariable String db, @PathVariable Integer dni, @PathVariable Integer profesion,
			Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			EstudioResponse actual = adapter.buscar(motor, dni, profesion);
			EstudioRequest estudio = new EstudioRequest(actual.getDniPersona(), actual.getIdProfesion(), actual.getFecha(),
					actual.getUniversidad(), motor);
			return formulario(model, estudio, ruta(motor, dni, profesion) , "Editar estudio", true);
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{dni}/{profesion}")
	public String editar(@PathVariable String db, @PathVariable Integer dni, @PathVariable Integer profesion,
			@ModelAttribute("estudio") EstudioRequest estudio, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		estudio.setDatabase(motor);
		try {
			adapter.editar(motor, dni, profesion, estudio);
			redirect.addFlashAttribute("mensaje", "Estudio actualizado.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, estudio, ruta(motor, dni, profesion), "Editar estudio", true);
		}
	}

	@GetMapping("/{db}/{dni}/{profesion}/eliminar")
	public String confirmarEliminar(@PathVariable String db, @PathVariable Integer dni, @PathVariable Integer profesion,
			Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			model.addAttribute("estudio", adapter.buscar(motor, dni, profesion));
			model.addAttribute("db", motor);
			model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
			model.addAttribute("dni", dni);
			model.addAttribute("profesion", profesion);
			return "estudio/confirmar";
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{dni}/{profesion}/eliminar")
	public String eliminar(@PathVariable String db, @PathVariable Integer dni, @PathVariable Integer profesion,
			RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			adapter.eliminar(motor, dni, profesion);
			redirect.addFlashAttribute("mensaje", "Estudio eliminado.");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
		}
		return "redirect:" + BASE + "?db=" + motor;
	}

	private String ruta(String motor, Integer dni, Integer profesion) {
		return BASE + "/" + motor + "/" + dni + "/" + profesion;
	}

	private String formulario(Model model, EstudioRequest estudio, String accion, String titulo, boolean editando) {
		model.addAttribute("estudio", estudio);
		model.addAttribute("accion", accion);
		model.addAttribute("titulo", titulo);
		model.addAttribute("editando", editando);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(estudio.getDatabase()));
		return "estudio/formulario";
	}
}
