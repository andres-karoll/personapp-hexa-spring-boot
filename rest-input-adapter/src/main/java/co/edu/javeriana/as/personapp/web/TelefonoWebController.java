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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Controller
@RequestMapping("/web/telefonos")
public class TelefonoWebController {

	private static final String BASE = "/web/telefonos";

	@Autowired
	private TelefonoInputAdapterRest adapter;

	@GetMapping
	public String listado(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		String motor = WebSupport.motor(db);
		model.addAttribute("db", motor);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
		try {
			model.addAttribute("telefonos", adapter.historial(motor));
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			model.addAttribute("telefonos", List.of());
		}
		return "telefono/listado";
	}

	@GetMapping("/nuevo")
	public String formularioCrear(@RequestParam(defaultValue = WebSupport.MARIA) String db, Model model) {
		TelefonoRequest telefono = new TelefonoRequest();
		telefono.setDatabase(WebSupport.motor(db));
		return formulario(model, telefono, BASE, "Nuevo teléfono", false);
	}

	@PostMapping
	public String crear(@ModelAttribute("telefono") TelefonoRequest telefono, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(telefono.getDatabase());
		try {
			adapter.crear(telefono);
			redirect.addFlashAttribute("mensaje", "Teléfono creado.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, telefono, BASE, "Nuevo teléfono", false);
		}
	}

	@GetMapping("/{db}/{numero}/editar")
	public String formularioEditar(@PathVariable String db, @PathVariable String numero, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			TelefonoResponse actual = adapter.buscar(motor, numero);
			TelefonoRequest telefono = new TelefonoRequest(actual.getNumero(), actual.getOperador(), actual.getDuenio(), motor);
			return formulario(model, telefono, BASE + "/" + motor + "/" + numero, "Editar teléfono", true);
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{numero}")
	public String editar(@PathVariable String db, @PathVariable String numero,
			@ModelAttribute("telefono") TelefonoRequest telefono, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		telefono.setDatabase(motor);
		try {
			adapter.editar(motor, numero, telefono);
			redirect.addFlashAttribute("mensaje", "Teléfono actualizado.");
			return "redirect:" + BASE + "?db=" + motor;
		} catch (Exception e) {
			model.addAttribute("error", WebSupport.mensaje(e));
			return formulario(model, telefono, BASE + "/" + motor + "/" + numero, "Editar teléfono", true);
		}
	}

	@GetMapping("/{db}/{numero}/eliminar")
	public String confirmarEliminar(@PathVariable String db, @PathVariable String numero, Model model, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			model.addAttribute("telefono", adapter.buscar(motor, numero));
			model.addAttribute("db", motor);
			model.addAttribute("dbNombre", WebSupport.nombreMotor(motor));
			model.addAttribute("numero", numero);
			return "telefono/confirmar";
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
			return "redirect:" + BASE + "?db=" + motor;
		}
	}

	@PostMapping("/{db}/{numero}/eliminar")
	public String eliminar(@PathVariable String db, @PathVariable String numero, RedirectAttributes redirect) {
		String motor = WebSupport.motor(db);
		try {
			adapter.eliminar(motor, numero);
			redirect.addFlashAttribute("mensaje", "Teléfono eliminado.");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", WebSupport.mensaje(e));
		}
		return "redirect:" + BASE + "?db=" + motor;
	}

	private String formulario(Model model, TelefonoRequest telefono, String accion, String titulo, boolean editando) {
		model.addAttribute("telefono", telefono);
		model.addAttribute("accion", accion);
		model.addAttribute("titulo", titulo);
		model.addAttribute("editando", editando);
		model.addAttribute("dbNombre", WebSupport.nombreMotor(telefono.getDatabase()));
		return "telefono/formulario";
	}
}
