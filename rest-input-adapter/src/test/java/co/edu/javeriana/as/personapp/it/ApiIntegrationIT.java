package co.edu.javeriana.as.personapp.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Pruebas de integración contra la API REST en ejecución (por defecto http://localhost:3000).
 * Se ejecutan con: scripts/run-integration-tests.sh  (o mvn -Pintegration test con la API arriba).
 * Cada prueba usa cédulas, ids y teléfonos aleatorios para no chocar con los datos de ejemplo.
 */
class ApiIntegrationIT {

	private static final String BASE = System.getProperty("api.url", "http://localhost:3000");
	private static final String API = BASE + "/api/v1";
	private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

	@BeforeAll
	static void apiMustBeUp() throws Exception {
		HttpResponse<Void> docs = CLIENT.send(HttpRequest.newBuilder(URI.create(BASE + "/api-docs")).build(),
				HttpResponse.BodyHandlers.discarding());
		if (docs.statusCode() != 200)
			throw new IllegalStateException("La API no responde en " + BASE
					+ ". Levántala con: docker compose up -d --build");
	}

	// ---------- CRUD por módulo ----------

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void personaCrud(String db) throws Exception {
		int dni = randomPersonId();
		expect(200, call("POST", "/persona", personaJson(dni, "Ana", db)));
		HttpResponse<String> get = call("GET", "/persona/" + db + "/" + dni, null);
		expect(200, get);
		assertThat(get.body()).contains("\"firstName\":\"Ana\"");

		expect(200, call("PUT", "/persona/" + db + "/" + dni, personaJson(dni, "Ana Maria", db)));
		assertThat(call("GET", "/persona/" + db + "/" + dni, null).body()).contains("Ana Maria");

		expect(204, call("DELETE", "/persona/" + db + "/" + dni, null));
		expect(404, call("GET", "/persona/" + db + "/" + dni, null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void profesionCrud(String db) throws Exception {
		int id = randomProfessionId();
		expect(200, call("POST", "/profesion", profesionJson(id, "Ingenieria Test", db)));
		assertThat(call("GET", "/profesion/" + db + "/" + id, null).body()).contains("Ingenieria Test");

		expect(200, call("PUT", "/profesion/" + db + "/" + id, profesionJson(id, "Ingenieria Editada", db)));
		assertThat(call("GET", "/profesion/" + db + "/" + id, null).body()).contains("Ingenieria Editada");

		expect(204, call("DELETE", "/profesion/" + db + "/" + id, null));
		expect(404, call("GET", "/profesion/" + db + "/" + id, null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void telefonoCrud(String db) throws Exception {
		int dueno = randomPersonId();
		String numero = randomPhone();
		expect(200, call("POST", "/persona", personaJson(dueno, "Dueno", db)));
		expect(200, call("POST", "/telefono", telefonoJson(numero, "Claro", dueno, db)));
		assertThat(call("GET", "/telefono/" + db + "/" + numero, null).body()).contains("\"operador\":\"Claro\"");

		expect(200, call("PUT", "/telefono/" + db + "/" + numero, telefonoJson(numero, "Tigo", dueno, db)));
		assertThat(call("GET", "/telefono/" + db + "/" + numero, null).body()).contains("\"operador\":\"Tigo\"");

		expect(204, call("DELETE", "/telefono/" + db + "/" + numero, null));
		expect(404, call("GET", "/telefono/" + db + "/" + numero, null));
		expect(204, call("DELETE", "/persona/" + db + "/" + dueno, null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void estudioCrud(String db) throws Exception {
		int persona = randomPersonId();
		int profesion = randomProfessionId();
		expect(200, call("POST", "/persona", personaJson(persona, "Estudiante", db)));
		expect(200, call("POST", "/profesion", profesionJson(profesion, "Carrera Test", db)));
		expect(200, call("POST", "/estudio", estudioJson(persona, profesion, "2020-01-15", "U Test", db)));
		assertThat(call("GET", "/estudio/" + db + "/" + persona + "/" + profesion, null).body())
				.contains("\"fecha\":\"2020-01-15\"");

		expect(200, call("PUT", "/estudio/" + db + "/" + persona + "/" + profesion,
				estudioJson(persona, profesion, "2021-06-30", "U Editada", db)));
		assertThat(call("GET", "/estudio/" + db + "/" + persona + "/" + profesion, null).body())
				.contains("\"fecha\":\"2021-06-30\"");

		expect(204, call("DELETE", "/estudio/" + db + "/" + persona + "/" + profesion, null));
		expect(404, call("GET", "/estudio/" + db + "/" + persona + "/" + profesion, null));
		expect(204, call("DELETE", "/profesion/" + db + "/" + profesion, null));
		expect(204, call("DELETE", "/persona/" + db + "/" + persona, null));
	}

	// ---------- reglas de negocio y errores ----------

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void duplicadosRespondenConflicto(String db) throws Exception {
		int dni = randomPersonId();
		expect(200, call("POST", "/persona", personaJson(dni, "Original", db)));
		expect(409, call("POST", "/persona", personaJson(dni, "Duplicada", db)));
		assertThat(call("GET", "/persona/" + db + "/" + dni, null).body()).contains("Original");
		expect(204, call("DELETE", "/persona/" + db + "/" + dni, null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void teléfonoConDuenoInexistenteRespondeConflicto(String db) throws Exception {
		expect(409, call("POST", "/telefono", telefonoJson(randomPhone(), "Claro", 999_999_999, db)));
	}

	@Test
	void baseDeDatosInvalidaRespondeBadRequest() throws Exception {
		expect(400, call("GET", "/persona/oracle", null));
		expect(400, call("POST", "/profesion", profesionJson(randomProfessionId(), "X", "oracle")));
	}

	// ---------- cascada (las bases pueden tener relaciones) ----------

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void eliminarPersonaEliminaSusTelefonosYEstudios(String db) throws Exception {
		int persona = randomPersonId();
		int profesion = randomProfessionId();
		String numero = randomPhone();
		expect(200, call("POST", "/persona", personaJson(persona, "Cascada", db)));
		expect(200, call("POST", "/profesion", profesionJson(profesion, "Carrera Cascada", db)));
		expect(200, call("POST", "/telefono", telefonoJson(numero, "Claro", persona, db)));
		expect(200, call("POST", "/estudio", estudioJson(persona, profesion, "2019-03-10", "U", db)));

		expect(204, call("DELETE", "/persona/" + db + "/" + persona, null));

		expect(404, call("GET", "/telefono/" + db + "/" + numero, null));
		expect(404, call("GET", "/estudio/" + db + "/" + persona + "/" + profesion, null));
		expect(204, call("DELETE", "/profesion/" + db + "/" + profesion, null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void eliminarProfesionEliminaSusEstudios(String db) throws Exception {
		int persona = randomPersonId();
		int profesion = randomProfessionId();
		expect(200, call("POST", "/persona", personaJson(persona, "Egresado", db)));
		expect(200, call("POST", "/profesion", profesionJson(profesion, "Carrera Borrable", db)));
		expect(200, call("POST", "/estudio", estudioJson(persona, profesion, "2018-12-01", "U", db)));

		expect(204, call("DELETE", "/profesion/" + db + "/" + profesion, null));

		expect(404, call("GET", "/estudio/" + db + "/" + persona + "/" + profesion, null));
		expect(204, call("DELETE", "/persona/" + db + "/" + persona, null));
	}

	// ---------- validación de entrada (400, nunca 409 ni 500) ----------

	@ParameterizedTest
	@ValueSource(strings = {"maria", "mongo"})
	void entradaInvalidaRespondeBadRequest(String db) throws Exception {
		String largo = "A".repeat(60);
		expect(400, call("POST", "/persona", "{\"dni\":\"abc\",\"firstName\":\"A\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"M\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/persona", "{\"dni\":\"-1\",\"firstName\":\"A\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"M\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/persona", "{\"dni\":\"700000001\",\"firstName\":\"A\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"X\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/persona", "{\"dni\":\"700000002\",\"firstName\":\"A\",\"lastName\":\"B\",\"age\":\"-5\",\"sex\":\"M\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/persona", "{\"dni\":\"700000003\",\"firstName\":\"" + largo + "\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"M\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/persona", "{\"dni\":\"700000004\",\"lastName\":\"B\",\"age\":\"1\",\"sex\":\"M\",\"database\":\"" + db + "\"}"));
		expect(400, call("POST", "/profesion", profesionJson(randomProfessionId(), "N".repeat(91), db)));
		expect(400, call("POST", "/telefono", telefonoJson("", "Claro", randomPersonId(), db)));
		expect(400, call("POST", "/telefono", telefonoJson("1".repeat(16), "Claro", randomPersonId(), db)));
		expect(400, call("POST", "/estudio", estudioJson(147258369, 3, "2020/01/01", "U", db)));
		expect(404, call("DELETE", "/telefono/" + db + "/0000000000", null));
	}

	// ---------- utilidades ----------

	private static HttpResponse<String> call(String method, String path, String body) throws Exception {
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(API + path)).timeout(Duration.ofSeconds(15));
		if (body == null) {
			builder.method(method, HttpRequest.BodyPublishers.noBody());
		} else {
			builder.method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
					.header("Content-Type", "application/json");
		}
		return CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
	}

	private static void expect(int status, HttpResponse<String> response) {
		assertThat(response.statusCode()).as("respuesta: " + response.body()).isEqualTo(status);
	}

	private static int randomPersonId() {
		return ThreadLocalRandom.current().nextInt(800_000_000, 899_999_999);
	}

	private static int randomProfessionId() {
		return ThreadLocalRandom.current().nextInt(900_000, 999_999);
	}

	private static String randomPhone() {
		return "3" + ThreadLocalRandom.current().nextLong(100_000_000L, 999_999_999L);
	}

	private static String personaJson(int dni, String nombre, String db) {
		return String.format(
				"{\"dni\":\"%d\",\"firstName\":\"%s\",\"lastName\":\"Test\",\"age\":\"30\",\"sex\":\"F\",\"database\":\"%s\"}",
				dni, nombre, db);
	}

	private static String profesionJson(int id, String nombre, String db) {
		return String.format("{\"id\":\"%d\",\"nombre\":\"%s\",\"descripcion\":\"Test\",\"database\":\"%s\"}",
				id, nombre, db);
	}

	private static String telefonoJson(String numero, String operador, int dueno, String db) {
		return String.format("{\"numero\":\"%s\",\"operador\":\"%s\",\"duenio\":\"%d\",\"database\":\"%s\"}",
				numero, operador, dueno, db);
	}

	private static String estudioJson(int persona, int profesion, String fecha, String universidad, String db) {
		return String.format(
				"{\"dniPersona\":\"%d\",\"idProfesion\":\"%d\",\"fecha\":\"%s\",\"universidad\":\"%s\",\"database\":\"%s\"}",
				persona, profesion, fecha, universidad, db);
	}
}
