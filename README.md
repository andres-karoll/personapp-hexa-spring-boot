# personapp-hexa-spring-boot

Laboratorio de Arquitectura Limpia (hexagonal): servicio web **PersonAPP** con CRUD sobre
**MariaDB** y **MongoDB**, con adaptadores de entrada **REST** y **CLI**.

Fork del repositorio plantilla de Andrés Sánchez (Pontificia Universidad Javeriana):
https://github.com/andres-karoll/personapp-hexa-spring-boot

## Inicio rápido con Docker (recomendado)

Requisitos: **Docker** con Docker Compose. No hace falta instalar Java, Maven, MariaDB ni MongoDB.

```bash
docker compose up -d --build      # levanta MariaDB, MongoDB, carga los scripts y arranca la API
```

Cuando termine (`docker compose ps` muestra `api` en `running`):

| Qué                | Dónde                                              |
|--------------------|----------------------------------------------------|
| API REST           | http://localhost:3000/api/v1/persona/maria         |
| Swagger UI         | http://localhost:3000/swagger-ui.html              |
| MariaDB            | `localhost:3307` (usuario `persona_db`, clave `persona_db`) |
| MongoDB            | `localhost:27017` (usuario `persona_db`, clave `persona_db`, base de autenticación `admin`) |

CLI (menú de consola, interactivo):

```bash
docker compose run --rm cli
```

Otros comandos:

```bash
docker compose logs -f api          # logs de la API
docker compose down                 # detiene los contenedores (conserva los datos)
docker compose down -v              # detiene y borra los datos; la próxima vez se vuelven a cargar los scripts
```

Ejecutar las pruebas unitarias dentro de Docker (se ejecutan también en cada `docker compose build`):

```bash
docker run --rm -v "$PWD":/src -w /src maven:3.9-eclipse-temurin-25 mvn -B verify
```

### Qué hace cada servicio de `docker-compose.yml`

| Servicio     | Imagen / build                        | Función |
|--------------|---------------------------------------|---------|
| `mariadb`    | `mariadb:latest`                      | Base relacional. Ejecuta `scripts/persona_ddl_maria.sql` y `persona_dml_maria.sql` al crearse. |
| `mongodb`    | `mongo:latest`                        | Base documental con autenticación. |
| `mongo-init` | `mongo:latest` + `docker/mongo-init.sh` | Ejecuta una vez el DDL (crea el usuario) y el DML de MongoDB. |
| `api`        | `Dockerfile` con `MODULE=rest-input-adapter` | API REST en el puerto 3000. Espera a que las bases estén listas. |
| `cli`        | `Dockerfile` con `MODULE=cli-input-adapter` | Perfil `cli`: solo se ejecuta con `docker compose run --rm cli`. |

El `Dockerfile` es multietapa: compila todos los módulos con Maven y JDK 25 (ejecutando las pruebas), y la imagen final solo lleva el JRE 25 y el jar del módulo, ejecutado por un usuario sin privilegios. La configuración de conexión de cada servicio se toma de variables de entorno (`SPRING_DATASOURCE_URL`, `SPRING_MONGODB_URI`), así que el mismo código funciona en Docker y en local.

---

## 1. Stack y versiones

| Componente        | Versión                         |
|-------------------|---------------------------------|
| JDK               | 25 (LTS)                        |
| Maven             | 3.9+                            |
| Spring Boot       | 4.1.2                           |
| Lombok            | 1.18.48                         |
| springdoc-openapi | 3.2.0 (Swagger UI)              |
| MariaDB Connector/J | 4.0.0                         |
| MariaDB           | 10.6+ (probado con 13.0.2)      |
| MongoDB           | 9.0 (probado con 9.0.4, imagen `mongo:latest`) |
| Docker            | Para levantar MongoDB (opcional)|

Puertos: MariaDB **3307**, MongoDB **27017**, API REST **3000**.

## 2. Arquitectura

```
common                  anotaciones (@Adapter, @UseCase, @Port, @Mapper) y excepciones
domain                  modelo: Person, Phone, Profession, Study
application             puertos de entrada/salida y casos de uso (sin dependencias de frameworks de persistencia)
maria-output-adapter    adaptador de salida MariaDB (JPA / Hibernate)
mongo-output-adapter    adaptador de salida MongoDB (Spring Data MongoDB)
rest-input-adapter      adaptador de entrada REST (Spring MVC + Swagger)  -> aplicación Spring Boot
cli-input-adapter       adaptador de entrada CLI (menú en consola)
```

Cada entidad (Persona, Profesión, Teléfono, Estudio) tiene su puerto de entrada, su caso de uso y un
puerto de salida con dos implementaciones (MariaDB y MongoDB). El adaptador REST elige la base de datos
según el parámetro `{database}` de la URL (`maria` o `mongo`).

## 3. Configuración del ambiente (ejecución local, sin Docker)

Solo si no vas a usar Docker. Con Docker, `docker compose up` hace todo lo de esta sección.

### 3.1 Bases de datos

**Opción con Docker para MongoDB** (puerto 27017, usuario root para crear el usuario de la aplicación):

```bash
docker run -d --name persona-mongo -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=root_pwd mongo:latest
```

**MariaDB** en el puerto 3307 (ver `rest-input-adapter/src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:mariadb://localhost:3307/persona_db
spring.datasource.username=persona_db
spring.datasource.password=persona_db
```

**MongoDB** en el puerto 27017. La conexión se configura con una URI que incluye `authSource=admin`
(en Spring Boot 4 las propiedades separadas `spring.data.mongodb.username/password` ya no aplican a la conexión):

```properties
spring.mongodb.uri=mongodb://persona_db:persona_db@localhost:27017/persona_db?authSource=admin
```

### 3.2 Scripts DDL y DML

Los scripts están en `scripts/`:

| Archivo                  | Descripción                                                   |
|--------------------------|---------------------------------------------------------------|
| `persona_ddl_maria.sql`  | Crea el usuario, el esquema `persona_db` y las 4 tablas (utf8mb4) |
| `persona_dml_maria.sql`  | Datos de ejemplo: personas, profesiones, teléfonos y estudios |
| `persona_ddl_mongo.js`   | Crea el usuario `persona_db` en la base `admin` (ejecutar con `mongosh`) |
| `persona_dml_mongo.js`   | Datos de ejemplo en la base `persona_db` (colecciones persona, profesion, telefono, estudios) |

Ejecución en MariaDB (en este orden, con el cliente en utf8mb4):

```bash
mariadb -uroot -P3307 -h127.0.0.1 --default-character-set=utf8mb4 < scripts/persona_ddl_maria.sql
mariadb -uroot -P3307 -h127.0.0.1 --default-character-set=utf8mb4 < scripts/persona_dml_maria.sql
```

Ejecución en MongoDB:

Con el contenedor de Docker (el DDL necesita el usuario root del contenedor):

```bash
docker exec -i persona-mongo mongosh -u root -p root_pwd --authenticationDatabase admin --quiet < scripts/persona_ddl_mongo.js
docker exec -i persona-mongo mongosh -u persona_db -p persona_db --authenticationDatabase admin --quiet < scripts/persona_dml_mongo.js
```

Con `mongosh` instalado localmente:

```bash
mongosh --port 27017 -u root -p root_pwd --authenticationDatabase admin < scripts/persona_ddl_mongo.js
mongosh --port 27017 -u persona_db -p persona_db --authenticationDatabase admin < scripts/persona_dml_mongo.js
```

> El DDL de MariaDB borra y recrea el esquema `persona_db`; los datos de ejemplo se pueden volver a cargar ejecutando ambos scripts.

### 3.3 IDE

Instalar el plugin de **Lombok** en el IDE (VS Code: extensión *Lombok Annotations Support*) y configurar el JDK 25 en el proyecto.

## 4. Compilación

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 25)   # macOS; en Linux apunte al JDK 25
mvn clean install -DskipTests
```

Para ejecutar las pruebas (si las hay): `mvn clean verify`.

## 5. Despliegue y ejecución

### 5.1 API REST

```bash
java -jar rest-input-adapter/target/rest-input-adapter-0.0.1-SNAPSHOT.jar
```

- API: `http://localhost:3000/api/v1/...`
- Swagger UI: `http://localhost:3000/swagger-ui.html`
- Documentación OpenAPI: `http://localhost:3000/api-docs`
- Logs: `rest-input-adapter/logs/persona.log`

### 5.2 CLI

```bash
java -jar cli-input-adapter/target/cli-input-adapter-0.0.1-SNAPSHOT.jar
```

El menú principal permite elegir el módulo (Personas, Profesiones, Teléfonos, Estudios), luego el motor de
persistencia (MariaDB o MongoDB) y después las operaciones: ver todos, buscar, crear, editar y eliminar.

## 6. Endpoints REST

`{database}` puede ser `maria` o `mongo`. Cualquier otro valor responde **400**.

### Persona

| Método | Ruta                                  | Descripción                  |
|--------|---------------------------------------|------------------------------|
| GET    | `/api/v1/persona/{database}`          | Listar personas              |
| GET    | `/api/v1/persona/{database}/{id}`     | Consultar una persona        |
| POST   | `/api/v1/persona`                     | Crear persona (body con `database`) |
| PUT    | `/api/v1/persona/{database}/{id}`     | Actualizar persona           |
| DELETE | `/api/v1/persona/{database}/{id}`     | Eliminar persona (204)       |

Body de ejemplo (POST / PUT):

```json
{ "dni": "555666777", "firstName": "Luis", "lastName": "Mora", "age": "40", "sex": "M", "database": "maria" }
```

### Profesión

| Método | Ruta                                  | Descripción                  |
|--------|---------------------------------------|------------------------------|
| GET    | `/api/v1/profesion/{database}`        | Listar profesiones           |
| GET    | `/api/v1/profesion/{database}/{id}`   | Consultar una profesión      |
| POST   | `/api/v1/profesion`                   | Crear profesión              |
| PUT    | `/api/v1/profesion/{database}/{id}`   | Actualizar profesión         |
| DELETE | `/api/v1/profesion/{database}/{id}`   | Eliminar profesión (204)     |

```json
{ "id": "10", "nombre": "Arquitecto", "descripcion": "Diseña edificios", "database": "maria" }
```

### Teléfono

| Método | Ruta                                  | Descripción                  |
|--------|---------------------------------------|------------------------------|
| GET    | `/api/v1/telefono/{database}`         | Listar teléfonos             |
| GET    | `/api/v1/telefono/{database}/{numero}`| Consultar un teléfono        |
| POST   | `/api/v1/telefono`                    | Crear teléfono               |
| PUT    | `/api/v1/telefono/{database}/{numero}`| Actualizar teléfono          |
| DELETE | `/api/v1/telefono/{database}/{numero}`| Eliminar teléfono (204)      |

```json
{ "numero": "3000000001", "operador": "Claro", "duenio": "147258369", "database": "maria" }
```

### Estudio (clave compuesta: persona + profesión)

| Método | Ruta                                                         | Descripción              |
|--------|--------------------------------------------------------------|--------------------------|
| GET    | `/api/v1/estudio/{database}`                                 | Listar estudios          |
| GET    | `/api/v1/estudio/{database}/{dniPersona}/{idProfesion}`      | Consultar un estudio     |
| POST   | `/api/v1/estudio`                                            | Crear estudio            |
| PUT    | `/api/v1/estudio/{database}/{dniPersona}/{idProfesion}`      | Actualizar estudio       |
| DELETE | `/api/v1/estudio/{database}/{dniPersona}/{idProfesion}`      | Eliminar estudio (204)   |

```json
{ "dniPersona": "147258369", "idProfesion": "2", "fecha": "2019-01-01", "universidad": "Universidad X", "database": "maria" }
```

### Códigos de respuesta

| Código | Cuándo                                                                 |
|--------|------------------------------------------------------------------------|
| 200    | Operación exitosa (GET, POST, PUT)                                     |
| 204    | Eliminación exitosa (DELETE)                                           |
| 400    | Base de datos inválida, tipo de dato inválido o campos obligatorios vacíos |
| 404    | El registro no existe                                                  |
| 409    | Ya existe el registro (POST), o referencia inexistente (p. ej. teléfono de una persona que no existe) |

Ejemplo:

```bash
curl -s http://localhost:3000/api/v1/persona/maria
curl -s -X POST http://localhost:3000/api/v1/telefono -H 'Content-Type: application/json' \
     -d '{"numero":"3000000001","operador":"Claro","duenio":"147258369","database":"maria"}'
curl -s -X DELETE http://localhost:3000/api/v1/telefono/maria/3000000001 -w "HTTP %{http_code}\n"
```

## 7. Notas de diseño

- **Relaciones sin ciclos al mapear.** Los objetos anidados (una persona dentro de un teléfono o de un estudio) se mapean de forma superficial (solo sus datos básicos). Mapear recursivamente persona ↔ estudios generaba recursión infinita en cuanto había datos.
- **MariaDB:** al eliminar una profesión o una persona se eliminan en cascada sus estudios (y en el caso de persona, sus teléfonos), por la configuración `CascadeType.ALL` de las entidades.
- **Cascada:** eliminar una persona borra sus teléfonos y estudios; eliminar una profesión borra sus estudios. En MariaDB lo hace JPA (`CascadeType.ALL`); en MongoDB lo hacen explícitamente los adaptadores.
- **MongoDB:** las referencias son `@DocumentReference` y se guardan como el id de la otra colección (no como `DBRef`). Se leen en modo eager para que la persona de un teléfono o un estudio venga completa. Antes de guardar un teléfono o un estudio se valida que la persona (y la profesión) existan; si no, se responde 409.
- **Fechas:** en MariaDB la fecha de grado es `LocalDate` (columna `DATE`). En MongoDB, Spring guarda las fechas de grado como medianoche en la zona horaria del sistema. Los datos de ejemplo usan `05:00Z`, que corresponde a medianoche en Colombia (UTC-5).
- **Charset:** el esquema y las tablas de MariaDB usan `utf8mb4` para conservar tildes y eñes.

## 8. Principios SOLID aplicados

| Principio | Dónde se aplica |
|-----------|-----------------|
| **S** – Responsabilidad única | Cada caso de uso (`PersonUseCase`, `ProfessionUseCase`, `PhoneUseCase`, `StudyUseCase`) solo contiene reglas de negocio. Cada adaptador solo traduce (REST/CLI ↔ dominio ↔ base de datos). Los mappers solo convierten. |
| **O** – Abierto/cerrado | Agregar un motor de persistencia es añadir una constante a `DatabaseOption` y un adaptador de salida con su `@Config`; los adaptadores de entrada no cambian (no hay `if` por motor). |
| **L** – Sustitución de Liskov | Todos los adaptadores de salida cumplen el contrato de sus puertos: un fallo de escritura se propaga como excepción en vez de reportarse como éxito (antes Mongo devolvía la persona aunque no se guardara). |
| **I** – Segregación de interfaces | Cada agregado tiene sus propios puertos de entrada y salida pequeños (`PersonInputPort`, `ProfessionOutputPort`, ...). Se eliminó `setPersintence` del puerto de entrada, que obligaba a depender de un detalle de salida. |
| **D** – Inversión de dependencias | Los adaptadores de entrada dependen de `PersistenceRegistry<Puerto>` (abstracción) y no de `MariaDB`/`MongoDB`. Los casos de uso reciben puertos por constructor. La configuración Spring (`PersistenceConfig`) es el único lugar que conoce las implementaciones. |

Los casos de uso se prueban sin Spring ni bases de datos, con puertos simulados (Mockito).

## 9. Pruebas

```bash
mvn clean verify                   # 21 pruebas unitarias (casos de uso, registro de motores); no necesitan bases ni API
scripts/run-integration-tests.sh   # levanta Docker Compose y corre 17 pruebas de integración contra la API
```

- **Unitarias** (`*Test`): casos de uso con puertos simulados (Mockito), registro de motores y enum.
- **Integración** (`*IT`, perfil Maven `integration`): CRUD completo de los cuatro módulos en MariaDB y MongoDB, duplicados (409), base inválida (400), referencias inexistentes (409) y cascadas en ambas bases.

## 10. Pendiente

- Documento final de la entrega (portada, marco conceptual, diseño, procedimiento, conclusiones y referencias).
