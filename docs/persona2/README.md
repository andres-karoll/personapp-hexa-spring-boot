# Persona 2: persistencia y Docker

Se conservan los siete módulos, Java 11 como destino, Spring Boot 2.7.18 y los puertos originales. No se modifica el núcleo ni los adaptadores de entrada. Los tres archivos locales de Profession existían antes de esta intervención; el repositorio y sus cinco pruebas se conservan. El adaptador recibe una frontera transaccional para leer sus relaciones LAZY.

## Diseño y contratos

Los adaptadores implementan los puertos de `application/port/out`. `save` inserta o actualiza por identificador; `find` lista; `findById` devuelve `null` si el identificador no existe. `delete` devuelve la comprobación de ausencia después del borrado. En MariaDB, borrar un identificador inexistente conserva el comportamiento de Spring Data JPA 2.7 (puede lanzar `EmptyResultDataAccessException`); MongoDB permite ese borrado. Los errores de integridad se propagan, nunca se convierten en un éxito aparente en los adaptadores nuevos.

| Dominio | MariaDB | MongoDB | Identificador |
|---|---|---|---|
| Profession | ProfesionEntity / ProfesionRepositoryMaria | ProfesionDocument / ProfesionRepositoryMongo | Integer |
| Study | EstudiosEntity / EstudiosRepositoryMaria | EstudiosDocument / EstudiosRepositoryMongo | EstudiosEntityPK(idProf, ccPer) / texto persona-profesion |
| Phone | TelefonoEntity / TelefonoRepositoryMaria | TelefonoDocument / TelefonoRepositoryMongo | número String |
| Person | PersonaEntity / PersonaRepositoryMaria | PersonaDocument / PersonaRepositoryMongo | Integer |

Los ocho beans de salida conservan las convenciones `person`, `profession`, `study`, `phone` + `OutputAdapterMaria` o `OutputAdapterMongo`.

## Relaciones e integridad

MariaDB almacena `estudios(id_prof, cc_per)` con clave compuesta; ambas columnas también son llaves foráneas. El mapper asigna tanto la PK como las referencias superficiales a Persona y Profesión. StudyOutputAdapterMaria carga los padres existentes dentro de una transacción; PhoneOutputAdapterMaria hace lo mismo con el dueño. Así los datos del padre vienen de la base y guardar un hijo no crea ni modifica sus padres.

Se retiran las cascadas REMOVE de Persona y Profesión conservando PERSIST y MERGE. La base rechaza borrar un padre referenciado; hay que eliminar primero los estudios o teléfonos explícitamente. Borrar un estudio o teléfono conserva sus padres. No se añade `orphanRemoval`.

MongoDB usa `@DocumentReference` en los hijos y consultas inversas `@ReadOnlyProperty` en los padres. No hay llaves foráneas de motor ni cascada de guardado: se crean primero personas/profesiones y luego teléfonos/estudios mediante sus adaptadores. Las listas de relaciones en un documento padre son vistas de lectura; guardarlas en el padre no persiste documentos hijos. Los adaptadores nuevos comprueban la existencia de los padres y se bloquea el borrado de personas/profesiones con relaciones.

Estas comprobaciones MongoDB protegen el flujo secuencial del laboratorio. No son una transacción entre colecciones: un cliente externo o escrituras concurrentes pueden crear referencias huérfanas. El despliegue original es un servidor standalone, sin replica set; no se promete atomicidad entre colecciones.

## Mappers

Se conservan los métodos `Shallow` de Samuel: al mapear un hijo, el padre incluye sus atributos pero no vuelve a recorrer las listas de hijos. Los padres sí convierten sus listas al leer. Esto acota la profundidad y evita recursión infinita.

EstudiosMapperMaria también contempla `java.sql.Date.toLocalDate()`; `java.sql.Date.toInstant()` lanza `UnsupportedOperationException`. Los valores opcionales siguen las convenciones de la plantilla (fechas nulas, textos opcionales normalizados a cadena vacía). Los mappers superficiales de lectura toleran relaciones sin resolver; en MariaDB los identificadores de Estudios pueden recuperarse desde su PK.

## Dependencias necesarias

`maria-output-adapter/pom.xml` declara spring-expression 5.3.31: la exclusión heredada de common deja sin ParserContext al contexto real de persistencia. Se corrige en el adaptador sin tocar common. En MongoDB spring-aop pasa de test a runtime/compile porque las referencias lazy necesitan proxies también al ejecutar la aplicación. No hay migración de frameworks.

## Despliegue

Desde la raíz del repositorio:

```bash
docker compose config --quiet
docker compose up -d --build rest
docker compose ps
docker compose logs rest
docker compose build cli
docker compose run --rm cli
```

REST publica http://localhost:3000 y Swagger http://localhost:3000/swagger-ui.html. `REST_PORT` permite cambiar el puerto publicado. CLI tiene perfil `cli`, entrada estándar abierta y TTY; `run --rm cli` lo habilita para una sesión interactiva. `docker compose up -d mariadb mongodb` inicia solamente las bases, útil al ejecutar Maven en el host.

Los Dockerfiles construyen con Maven 3.9.12/JDK 21 y ejecutan con JRE 11. Compilan el reactor requerido, instalan sus artefactos dentro de la etapa de construcción y ejecutan explícitamente `spring-boot:repackage`; no requieren modificar los POM de REST/CLI ni instalar Java/Maven en el host. Las pruebas se ejecutan por separado, no durante la construcción de imágenes.

La red predeterminada de Compose resuelve `mariadb:3306` y `mongodb:27017`; `localhost` dentro de una aplicación apuntaría al propio contenedor. Las variables Spring sobreescriben las propiedades locales sin editar los adaptadores de entrada. Ambas aplicaciones esperan el estado healthy de las bases. La salud REST comprueba apertura de su puerto TCP; no equivale a una prueba CRUD.

Se conservan los volúmenes `mariadb_data` y `mongodb_data` y los scripts SQL/JS existentes. Se ejecutan solo con un volumen vacío, en orden DDL/DML. Los scripts crean el usuario `persona_db` con contraseña `persona_db`. `DB_USER`, `DB_PASSWORD`, `MONGO_USER`, `MONGO_PASSWORD` cambian las credenciales usadas por las apps; deben coincidir con usuarios creados en las bases, no modifican los scripts ni usuarios de volúmenes existentes. Credenciales incluidas para el laboratorio local. No borrar volúmenes para actualizar las aplicaciones.

## Pruebas reproducibles

```bash
docker compose up -d mariadb mongodb
mvn clean install
mvn clean install -Dpersistence.integration=true
```

La primera ejecución corre pruebas unitarias y omite las dos integraciones externas por defecto. La segunda habilita ambas: no usa H2 ni Mongo embebido. Cada integración genera identificadores negativos aleatorios y limpia únicamente sus registros en `finally`; nunca borra colecciones, tablas ni volúmenes.

Las integraciones prueban CRUD, inexistentes, actualizaciones, padres, listas inversas, dos claves de Estudios para una persona y rechazo del borrado de padres referenciados. MariaDB también verifica con SQL directo la PK y las FK de Estudios y Teléfono. Los resultados de ejecución están en RESULTADOS.md y en `target/surefire-reports` de cada módulo.

Para otra conexión se pueden definir las variables `P2_MARIA_URL`, `P2_MARIA_USER`, `P2_MARIA_PASSWORD` y `P2_MONGO_URI`. Las bases deben contener el esquema y el usuario de los scripts del proyecto.

## Límites de otras responsabilidades

Persona 3: la entrada actual implementa Persona; faltan endpoints, DTOs y menús para Profession, Study y Phone. MenuPrincipal muestra avisos de implementación en esas tres opciones. No se modifican esos archivos.

Persona 1: los casos de uso de Profession, Study y Phone inyectan MariaDB por defecto; la futura entrada puede seleccionar sus adaptadores Mongo como ya hace Persona. El estado mutable de selección en PersonaInputAdapterRest pertenece a la entrada y puede requerir revisión de concurrencia. PersonOutputAdapterMongo.save conserva el manejo heredado de MongoWriteException; no se amplía aquí su contrato ni se afirma que ese manejo garantice persistencia en caso de error.
