# Cómo trabajar en este repositorio

Guía del equipo para el Laboratorio 2 (PersonAPP hexagonal). Léela antes de tu primer commit.

## 1. Responsabilidades

| Persona | Rama | Módulos / carpetas |
|---|---|---|
| Persona 1 — Núcleo y Maven | `persona1-nucleo` | `common/`, `domain/`, `application/`, `pom.xml` raíz, CI |
| Persona 2 — Persistencia y Docker | `persona2-persistencia` | `maria-output-adapter/`, `mongo-output-adapter/`, `scripts/`, `docker-compose.yml`, Dockerfiles |
| Persona 3 — REST, CLI y Swagger | `persona3-rest-cli` | `rest-input-adapter/`, `cli-input-adapter/` |

Si necesitas cambiar algo de otro módulo, **avisa primero** a su responsable (o pídele que lo haga).

## 2. Flujo de ramas

- `main` es la rama estable. **Nunca se hace push directo a `main`**: todo entra por Pull Request.
- Cada persona trabaja en su rama:

```bash
git checkout main
git pull origin main
git checkout -b persona2-persistencia   # o persona3-rest-cli
```

- Mantén tu rama al día con `main` (hazlo al empezar el día y antes de abrir el PR):

```bash
git checkout persona2-persistencia
git pull origin main          # trae lo nuevo de main a tu rama
# si hay conflictos: resuélvelos, mvn clean install, git add . y git commit
git push origin persona2-persistencia
```

- Abre el PR de tu rama hacia `main` en GitHub. El CI debe quedar en verde y al menos otra persona
  debe revisarlo antes del merge.

## 3. Convención de commits

Formato: `tipo(scope): descripción en español, en presente y en minúscula`.

| Tipo | Uso |
|---|---|
| `feat` | Funcionalidad nueva (adaptador, endpoint, menú) |
| `fix` | Corrección de un error |
| `test` | Pruebas nuevas o corregidas |
| `docs` | Documentación |
| `build` | Maven, dependencias, Docker |
| `ci` | GitHub Actions |
| `refactor` | Cambio interno sin cambiar comportamiento |

El `scope` es el módulo: `domain`, `application`, `maria-output-adapter`, `mongo-output-adapter`,
`rest-input-adapter`, `cli-input-adapter`, `scripts`.

Ejemplos:

```
feat(maria-output-adapter): adaptador de salida de profesión para MariaDB
fix(rest-input-adapter): responder 404 cuando la persona no existe
test(mongo-output-adapter): pruebas del mapper de teléfono
```

Commits pequeños: un cambio lógico por commit.

## 4. Contratos que no se cambian sin avisar

Los adaptadores dependen de estos contratos del módulo `application`. Cambiarlos rompe el trabajo de
los demás, así que cualquier cambio se acuerda en el grupo primero.

- **Puertos de entrada** (`application/port/in`): `create`, `edit`, `drop`, `findAll`, `findOne`, `count`
  (+ `getPhones` y `getStudies` en Persona) y `setPersintence` (con ese nombre, aunque tenga typo).
- **Puertos de salida** (`application/port/out`): `save`, `delete`, `find`, `findById`.
  `findById` devuelve `null` si no existe; `delete` devuelve `true` si quedó borrado.
- **Identificadores**: Persona `Integer identification`, Profesión `Integer identification`,
  Teléfono `String number`, Estudio `(Integer professionId, Integer personId)` en ese orden.
- **Nombres de beans** (`@Adapter("...")`): `personOutputAdapterMaria/Mongo`,
  `professionOutputAdapterMaria/Mongo`, `studyOutputAdapterMaria/Mongo`, `phoneOutputAdapterMaria/Mongo`.
- **Excepción** de "no existe": `NoExistException` de `common`.
- **Dominio** (`domain`): no se cambian clases ni el enum `Gender` (MALE, FEMALE, OTHER).
- **Mappers de salida**: al convertir la persona o la profesión relacionada desde `EstudiosMapper` o
  `TelefonoMapper`, usar siempre `fromDomainToAdapterShallow` / `fromAdapterToDomainShallow`
  (sin listas) para no caer en recursión infinita.

Detalle completo de los contratos: [docs/persona1/README.md](docs/persona1/README.md).

## 5. Checklist antes de abrir un PR

- [ ] `git pull origin main` hecho y conflictos resueltos.
- [ ] `mvn clean install` desde la raíz: BUILD SUCCESS.
- [ ] Todas las pruebas pasan, y agregaste pruebas para lo nuevo.
- [ ] Probado contra MARIA y contra MONGO con `docker compose up -d`.
- [ ] Sin código muerto, sin `System.out` de depuración y sin TODOs nuevos.
- [ ] Documentaste lo nuevo (README, `docs/`).
- [ ] No tocaste módulos de otra persona sin avisar.
- [ ] Mensajes de commit con la convención de la sección 3.

## 6. Entorno

- JDK 11 o superior (se compila con `--release 11`; con JDK 17 o 21 también funciona).
- Maven 3.8+.
- Docker Desktop (para las bases de datos).
- Plugin de Lombok en el IDE (Eclipse/STS: ejecutar el jar de Lombok; IntelliJ: activar
  *annotation processing*).
- El repo trae `.editorconfig` (tabs en Java, UTF-8) y `.gitattributes` (saltos de línea LF).
