# Resultados y entrega de Persona 2

Fecha: 11 de noviembre de 2026 (America/Bogota). Rama encontrada y conservada: persona2-persistencia-docker. No se hicieron commits, pushes, cambios de rama ni cambios persistentes de configuración Git. Para inspeccionar se usó safe.directory solo como opción temporal del comando, porque la cuenta sandbox tiene un propietario distinto.

## Estado inicial preservado

Los siguientes archivos ya eran trabajo local del usuario, sin seguimiento de Git; no se presentan como creaciones de esta intervención:

- maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/ProfessionOutputAdapterMaria.java — modificado únicamente para añadir transacción.
- maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/repository/ProfesionRepositoryMaria.java — contenido conservado.
- maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/ProfessionOutputAdapterMariaTest.java — contenido conservado.

No se encontraron archivos AGENTS.md en el workspace. Se inspeccionaron los puertos, casos de uso, mappers Shallow, documentos, entidades, scripts y configuración existentes antes de editar. Permanecen intactos domain, application, common y los adaptadores de entrada.

## Archivos creados en esta intervención

| Archivo exacto | Necesidad |
|---|---|
| [.dockerignore](../../.dockerignore) | Excluye Git, artefactos Maven y logs del contexto de construcción. |
| [Dockerfile.cli](../../Dockerfile.cli) | Compilación multietapa y jar ejecutable para CLI, ejecutado con JRE 11. |
| [Dockerfile.rest](../../Dockerfile.rest) | Compilación multietapa y jar ejecutable para REST, ejecutado con JRE 11. |
| [docs/persona2/GUIA_ESTUDIO.md](../../docs/persona2/GUIA_ESTUDIO.md) | Guía individual de sustentación con archivos, métodos y líneas consultadas. |
| [docs/persona2/README.md](../../docs/persona2/README.md) | Añade configuración, diseño, relaciones y comandos de Persona 2; en la raíz actualiza el estado y conserva el resto de la documentación. |
| [docs/persona2/RESULTADOS.md](../../docs/persona2/RESULTADOS.md) | Inventario exacto, justificación y evidencia de ejecución. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/PhoneOutputAdapterMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/PhoneOutputAdapterMaria.java) | Implementa el puerto y bean existentes para CRUD; valida/carga padres en hijos y protege profesiones referenciadas en Mongo. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/StudyOutputAdapterMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/StudyOutputAdapterMaria.java) | Implementa el puerto y bean existentes para CRUD; valida/carga padres en hijos y protege profesiones referenciadas en Mongo. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/repository/EstudiosRepositoryMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/repository/EstudiosRepositoryMaria.java) | Repositorio Spring Data con el tipo de ID original. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/repository/TelefonoRepositoryMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/repository/TelefonoRepositoryMaria.java) | Repositorio Spring Data con el tipo de ID original. |
| [maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/PersistenceIntegrationTest.java](../../maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/PersistenceIntegrationTest.java) | Prueba real CRUD, relaciones e integridad sobre el motor, con limpieza limitada a sus propios registros. |
| [maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/PhoneOutputAdapterMariaTest.java](../../maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/PhoneOutputAdapterMariaTest.java) | Comprueba CRUD, identificadores y delegación al repositorio mediante Mockito. |
| [maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/StudyOutputAdapterMariaTest.java](../../maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/adapter/StudyOutputAdapterMariaTest.java) | Comprueba CRUD, identificadores y delegación al repositorio mediante Mockito. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/PhoneOutputAdapterMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/PhoneOutputAdapterMongo.java) | Implementa el puerto y bean existentes para CRUD; valida/carga padres en hijos y protege profesiones referenciadas en Mongo. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/ProfessionOutputAdapterMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/ProfessionOutputAdapterMongo.java) | Implementa el puerto y bean existentes para CRUD; valida/carga padres en hijos y protege profesiones referenciadas en Mongo. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/StudyOutputAdapterMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/StudyOutputAdapterMongo.java) | Implementa el puerto y bean existentes para CRUD; valida/carga padres en hijos y protege profesiones referenciadas en Mongo. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/EstudiosRepositoryMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/EstudiosRepositoryMongo.java) | Repositorio Spring Data con el tipo de ID original; añade consultas de existencia de referencias para proteger los borrados. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/ProfesionRepositoryMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/ProfesionRepositoryMongo.java) | Repositorio Spring Data con el tipo de ID original. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/TelefonoRepositoryMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/repository/TelefonoRepositoryMongo.java) | Repositorio Spring Data con el tipo de ID original; añade consultas de existencia de referencias para proteger los borrados. |
| [mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/PersistenceIntegrationTest.java](../../mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/PersistenceIntegrationTest.java) | Prueba real CRUD, relaciones e integridad sobre el motor, con limpieza limitada a sus propios registros. |
| [mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/PhoneOutputAdapterMongoTest.java](../../mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/PhoneOutputAdapterMongoTest.java) | Comprueba CRUD, identificadores y delegación al repositorio mediante Mockito. |
| [mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/ProfessionOutputAdapterMongoTest.java](../../mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/ProfessionOutputAdapterMongoTest.java) | Comprueba CRUD, identificadores y delegación al repositorio mediante Mockito. |
| [mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/StudyOutputAdapterMongoTest.java](../../mongo-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mongo/adapter/StudyOutputAdapterMongoTest.java) | Comprueba CRUD, identificadores y delegación al repositorio mediante Mockito. |

## Archivos modificados en esta intervención

| Archivo exacto | Necesidad |
|---|---|
| [README.md](../../README.md) | Añade configuración, diseño, relaciones y comandos de Persona 2; en la raíz actualiza el estado y conserva el resto de la documentación. |
| [docker-compose.yml](../../docker-compose.yml) | Conserva servicios y volúmenes de BD; añade REST/CLI, red por servicios, entorno y dependencias healthy. |
| [maria-output-adapter/pom.xml](../../maria-output-adapter/pom.xml) | Declara spring-expression necesario para cargar el contexto real JPA. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/ProfessionOutputAdapterMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/adapter/ProfessionOutputAdapterMaria.java) | Añade únicamente frontera transaccional al adaptador preexistente para mapear relaciones LAZY. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/entity/PersonaEntity.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/entity/PersonaEntity.java) | Sustituye cascada ALL por PERSIST/MERGE para impedir borrados accidentales de hijos. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/entity/ProfesionEntity.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/entity/ProfesionEntity.java) | Sustituye cascada ALL por PERSIST/MERGE para impedir borrados accidentales de hijos. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/EstudiosMapperMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/EstudiosMapperMaria.java) | Asigna los dos padres, convierte java.sql.Date y recupera IDs de la PK cuando faltan relaciones. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/PersonaMapperMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/PersonaMapperMaria.java) | Permite leer una referencia incompleta sin desreferenciar null; conserva los métodos Shallow. |
| [maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/ProfesionMapperMaria.java](../../maria-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mariadb/mapper/ProfesionMapperMaria.java) | Permite leer una referencia incompleta sin desreferenciar null; conserva los métodos Shallow. |
| [maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/mapper/EstudiosMapperMariaTest.java](../../maria-output-adapter/src/test/java/co/edu/javeriana/as/personapp/mariadb/mapper/EstudiosMapperMariaTest.java) | Añade regresiones de fecha SQL y asignación de referencias sin cambiar las pruebas originales. |
| [mongo-output-adapter/pom.xml](../../mongo-output-adapter/pom.xml) | Hace disponible spring-aop en ejecución para las referencias lazy MongoDB. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/PersonOutputAdapterMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/adapter/PersonOutputAdapterMongo.java) | Bloquea el borrado de personas referenciadas por teléfonos o estudios. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/mapper/PersonaMapperMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/mapper/PersonaMapperMongo.java) | Permite leer una referencia incompleta sin desreferenciar null; conserva los métodos Shallow. |
| [mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/mapper/ProfesionMapperMongo.java](../../mongo-output-adapter/src/main/java/co/edu/javeriana/as/personapp/mongo/mapper/ProfesionMapperMongo.java) | Permite leer una referencia incompleta sin desreferenciar null; conserva los métodos Shallow. |

## Compilación y pruebas ejecutadas

Comando final: mvn -B clean install -Dpersistence.integration=true. Resultado BUILD SUCCESS en los siete módulos y el POM agregador; destino de compilación release 11 usando JDK 21/Maven 3.9.12.

| Módulo con pruebas | Ejecutadas | Fallos | Errores | Omitidas |
|---|---:|---:|---:|---:|
| application | 34 | 0 | 0 | 0 |
| maria-output-adapter | 24 | 0 | 0 | 0 |
| mongo-output-adapter | 22 | 0 | 0 | 0 |
| rest-input-adapter | 9 | 0 | 0 | 0 |
| Total | 89 | 0 | 0 | 0 |

Se incluyen las cinco pruebas preexistentes de Profession MariaDB. Las integraciones de persistencia son dos métodos de escenario completo: uno ejecutado sobre MariaDB 10.11 y otro sobre MongoDB 6.0. No se usaron motores embebidos, H2 ni repositorios simulados para esas integraciones. Sin la propiedad persistence.integration, esas dos pruebas externas se omiten y las unitarias permanecen activas.

Verificado con datos propios de prueba:

- Crear, recuperar, listar, actualizar y eliminar Profession, Study y Phone en ambos motores.
- Devolver null al buscar identificadores inexistentes.
- Recuperar dueño del teléfono y ambos padres del estudio desde persistencia real.
- Leer listas inversas de estudios/teléfonos desde Persona y estudios desde Profesión.
- Mantener dos estudios de la misma persona con profesiones distintas y eliminar solo uno.
- Conservar fecha DATE, incluida fecha bisiesta, y permitir fecha nula.
- Rechazar referencias inexistentes al guardar hijos y rechazar borrado de padres con relaciones.
- En MariaDB, rechazar con SQL directo duplicación de PK, las dos FK de Estudios y la FK de Teléfono.
- Conservar padres al borrar hijos. Limpiar los registros creados por las pruebas sin borrar datos existentes.

## Docker ejecutado

| Comando o comprobación | Resultado real |
|---|---|
| docker compose config --quiet | Configuración válida. |
| docker compose build rest cli | Ambas imágenes construidas correctamente. |
| docker compose up -d --no-build --wait rest | MariaDB, MongoDB y REST healthy. |
| GET http://localhost:3000/api/v1/persona/maria | HTTP 200. |
| GET http://localhost:3000/api/v1/persona/mongo | HTTP 200. |
| GET http://localhost:3000/api-docs | HTTP 200. |
| Entrada 0 enviada a docker compose run --rm -T --no-deps cli | Menú visible, entrada aceptada, FINISHED y cierre limpio de la aplicación. |

La comprobación CLI automatizada usa stdin sin TTY; el servicio conserva tty y stdin_open para una sesión humana con docker compose run --rm cli. REST queda ejecutándose en el puerto 3000. Las bases y sus volúmenes preexistentes se conservaron. No se reejecutaron los scripts de inicialización en un volumen nuevo durante esta intervención; se mantuvo su montaje y orden original.

Los logs locales de ejecución son build-persona2.log, docker-build-persona2.log, docker-run-persona2.log, docker-cli-persona2.log y docker-http-persona2.log; están excluidos por la regla *.log existente. Los resultados JUnit están en target/surefire-reports de cada módulo.

## Problemas encontrados y solución

- EstudiosMapperMaria creaba solo la PK y no los objetos padre: se asignan referencias Shallow y el adaptador carga los padres reales.
- DATE de MariaDB llega como java.sql.Date: se utiliza toLocalDate en vez de toInstant.
- CascadeType.ALL incluía REMOVE: se conserva PERSIST/MERGE y las FK bloquean borrar padres con hijos.
- El contexto JPA aislado falló por ausencia de ParserContext debido a la exclusión heredada: se declara spring-expression en el módulo Maria, sin tocar common.
- Las referencias lazy Mongo necesitan proxies en ejecución: spring-aop deja de tener alcance exclusivo de test.
- Un ajuste temporal enlazó hijos con el padre completo y una prueba existente detectó que perdía el límite Shallow: se retiró ese ajuste y se conservaron las aserciones originales.
- Maven requiere package en la misma invocación que repackage para asignar el artefacto fuente: los Dockerfiles lo ejecutan así después de instalar el reactor requerido. Se añadió caché Maven de BuildKit para reutilizar descargas.

## Comandos para usar la solución

Desde la raíz del repositorio:

```bash
docker compose up -d --build rest
docker compose ps
docker compose build cli
docker compose run --rm cli
mvn clean install -Dpersistence.integration=true
```

Para probar solo desde Maven: docker compose up -d mariadb mongodb. Los puertos del host son 3307 (MariaDB), 27017 (MongoDB) y 3000 (REST). Entre contenedores se usan mariadb:3306 y mongodb:27017.

## Pendientes y límites por responsabilidad

Persona 2: CRUD y relaciones de los componentes solicitados verificados. No se promete atomicidad entre colecciones en MongoDB standalone ni protección ante clientes que escriban directamente omitiendo los adaptadores. El guardado de listas ReadOnlyProperty del padre no guarda hijos en Mongo: se usan los adaptadores de hijos explícitamente.

Persona 3: faltan entradas REST/CLI completas de Profession, Study y Phone; MenuPrincipal conserva sus avisos de implementación. No se agregaron endpoints, DTOs, controladores ni menús. Los HTTP 200 comprueban los endpoints ya existentes de Persona, no endpoints nuevos de los otros dominios.

Persona 1: se conservan los puertos, los constructores de los casos de uso y la selección Maria por defecto. La futura entrada debe seleccionar el adaptador correspondiente. El manejo heredado de MongoWriteException en PersonOutputAdapterMongo.save sigue siendo una limitación del comportamiento original; no se afirma que devuelva prueba de persistencia cuando ocurre ese error. La selección mutable de persistencia en PersonaInputAdapterRest merece revisión de concurrencia por Persona 3.

La guía individual está en GUIA_ESTUDIO.md; el diseño y configuración detallados están en README.md de esta carpeta.
