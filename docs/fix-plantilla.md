# Corrección de la plantilla (rama `fix-plantilla`)

Errores que ya traía la plantilla del profesor y cómo se corrigieron. Solo se corrigió lo existente:
no se crearon adaptadores, endpoints, menús ni casos de uso nuevos, y no se cambiaron los contratos de
`application` ni el dominio.

| # | Archivo | Error | Causa | Corrección |
|---|---|---|---|---|
| 1 | `maria-output-adapter/.../EstudiosMapperMaria.java`, `mongo-output-adapter/.../EstudiosMapperMongo.java` | `fromAdapterToDomain` siempre devolvía `null`. | Terminaba en `return null;` después de llenar el objeto. | `return study;`. |
| 2 | `PersonaMapper*`, `ProfesionMapper*`, `EstudiosMapper*`, `TelefonoMapper*` (Maria y Mongo, 8 mappers) | Recursión infinita (`StackOverflowError`) al mapear una persona con estudios o teléfonos, o una profesión con estudios. | `PersonaMapper` mapea sus estudios con `EstudiosMapper`, que volvía a mapear la persona completa con `PersonaMapper` (y lo mismo con `TelefonoMapper` y `ProfesionMapper`). | Nuevos métodos públicos `fromDomainToAdapterShallow` / `fromAdapterToDomainShallow` en `PersonaMapper` y `ProfesionMapper` (solo campos simples, sin listas). Los mapeos completos los reutilizan y agregan las listas; `EstudiosMapper` y `TelefonoMapper` usan los superficiales. |
| 3 | `PersonaMapperMaria.java`, `PersonaMapperMongo.java` | `Gender.OTHER` se guardaba como `' '` / `" "`, que la columna `ENUM('M','F')` rechaza; con `null` el error era un `NullPointerException` poco claro. | El mapeo usaba `' '` como valor por defecto. | `IllegalArgumentException` con mensaje claro si el género es `OTHER` o `null`. El enum no se cambió. |
| 4 | `rest-input-adapter/.../PersonaMapperRest.java` | `fromAdapterToDomain` era un TODO que devolvía `new Person()` vacío: el POST creaba personas sin datos. | Método sin implementar. | `dni` → `identification` (Integer), `firstName`, `lastName`, `age` (Integer, `null` si viene vacía) y `sex` `M`/`F` sin importar mayúsculas → `MALE`/`FEMALE`. Datos inválidos → `IllegalArgumentException` con mensaje. |
| 5 | `rest-input-adapter/.../PersonaInputAdapterRest.java` | `crearPersona` respondía siempre "MariaDB" aunque se eligiera Mongo, y con opción inválida retornaba `null`. | Usaba fijo `fromDomainToAdapterRestMaria`; el `catch` solo registraba el error. | Responde según la BD elegida; con opción o datos inválidos retorna un `PersonaResponse` con los datos recibidos y `status = "ERROR: <motivo>"`. La comparación de la opción también acepta `database` nulo. |
| 6 | `rest-input-adapter/.../PersonaMapperRest.java` | La respuesta mostraba `"age": "null"` cuando la edad era nula. | Concatenación `person.getAge()+""`. | Edad nula → `""`. |
| 7 | `scripts/persona_dml_mongo.js` | Los datos se insertaban en `prueba_db`. | `use prueba_db`. | Se usa `persona_db`. |
| 8 | `cli-input-adapter/.../PersonaInputAdapterCli.java` | `historial1()` duplicaba `historial()` y no se usaba. | Código muerto. | Se eliminó (y sus imports). |
| 9 | `rest-input-adapter/.../PersonaControllerV1.java` | Log copiado de otro proyecto: "crearTarea". | Copiar y pegar. | `"Into crearPersona REST API"`, igual que el otro log. |
| 10 | `scripts/persona_ddl_mongo.js`, `scripts/persona_dml_mongo.js` | En el contenedor de Mongo los scripts terminaban sin error pero no creaban ni el usuario ni los datos. | El helper `use <db>` de mongosh no se ejecuta dentro de archivos `.js` (scripts de init). | `db = db.getSiblingDB("admin")` / `db.getSiblingDB("persona_db")`, que funciona igual en la shell interactiva. El modelo no cambió. |
| 11 | `.gitignore` | No ignoraba `logs/` (la app escribe `logs/persona.log`), `.DS_Store` ni `.flattened-pom.xml`. | Plantilla genérica. | Entradas agregadas. |

## Revisado y sin cambios

- `spring.main.allow-circular-references=true` **se mantiene**: aunque ya no hay recursión al mapear,
  los beans siguen dependiendo unos de otros (`PersonaMapper` ↔ `EstudiosMapper`, `PersonaMapper` ↔
  `TelefonoMapper`, `ProfesionMapper` ↔ `EstudiosMapper`) por inyección de campos. Quitarlo haría
  fallar el arranque.
- `persona_ddl_maria.sql` y `persona_dml_maria.sql` funcionan tal cual en el contenedor de MariaDB.
- Al leer de la BD, un género distinto de `M`/`F` sigue mapeándose a `OTHER` (no se lanza excepción al
  leer, para no romper consultas por un dato inesperado).

## Observaciones para Persona 2 (no corregidas, son parte de su trabajo)

- `EstudiosMapperMaria.fromDomainToAdapter` no asigna `persona` ni `profesion` a la entidad (son
  `@ManyToOne(optional = false)`); revisarlo al implementar el adaptador de Estudio.
- `PersonOutputAdapterMongo.save` atrapa `MongoWriteException` y devuelve la persona sin guardar.

## Para Persona 3

- Los errores de `crearPersona` hoy se devuelven con HTTP 200 y `status = "ERROR: ..."`; al pasar a
  `ResponseEntity` conviene responder 400/404.

## Verificación

- `mvn clean install` desde la raíz: **BUILD SUCCESS**, 54 pruebas (34 del núcleo, 6 Maria, 5 Mongo,
  9 REST), 0 fallos.
- `docker compose up -d`: ambos contenedores `healthy`; MariaDB con las 4 tablas y 5 personas; MongoDB
  con el usuario `persona_db` en `admin` y 5 personas en `persona_db`.
- API REST con `--spring.main.lazy-initialization=true`: `GET /api/v1/persona/maria`, `GET
  /api/v1/persona/mongo`, `POST` en ambas bases, `POST` con BD o sexo inválidos (status ERROR) y Swagger
  (`/swagger-ui.html` → 302 a `/swagger-ui/index.html`, `/api-docs` 200). CLI: "ver todas las personas"
  en ambos motores.
- Arranque normal (sin lazy): falla **solo** porque faltan los beans `phoneOutputAdapterMaria`,
  `professionOutputAdapterMaria` y `studyOutputAdapterMaria` (pendiente esperado de Persona 2).
