# Revisión del núcleo (Persona 1) — common, domain, application y Maven

Revisión hecha sobre `main` (commit `b9bc9c8`) antes de editar. Línea base: `mvn clean install`
compila completo con JDK 17.0.20 (target Java 11).

| Archivo | Problema | Severidad | Corrección propuesta |
|---|---|---|---|
| `application/.../usecase/PersonUseCase.java` (`edit`) | No asigna el `identification` recibido al objeto antes de guardar: si el cuerpo trae otra cédula (o ninguna) se guarda/crea un registro distinto al que se pidió editar. | Alta | `person.setIdentification(identification)` antes de `save`. |
| `domain/.../Person.java` (`isValidAge`) | `this.age >= 0` hace auto-unboxing de `age`; si `age` es `null` lanza `NullPointerException`, pero la edad es opcional (`edad NULL`). | Alta | `return this.age == null \|\| this.age >= 0;` (null = válida). |
| `domain/.../Person.java`, `Profession.java` | `@Data` genera `equals/hashCode` con **todos** los campos, incluidas las listas `phoneNumbers` y `studies`. Con relaciones bidireccionales (`Person -> Study -> Person`, `Person -> Phone -> Person`, `Profession -> Study -> Profession`) `hashCode()`/`equals()` entran en recursión infinita (`StackOverflowError`), p. ej. al meter objetos en un `Set` o comparar en pruebas. `toString` ya estaba protegido con `@ToString.Exclude`, `equals/hashCode` no. | Alta | `@EqualsAndHashCode.Exclude` en `Person.phoneNumbers`, `Person.studies` y `Profession.studies` (igual que ya se hace con `@ToString.Exclude`). |
| `domain/.../Phone.java` | Tiene campos `@NonNull` pero no `@RequiredArgsConstructor` (las demás clases del dominio sí). Como hay `@NoArgsConstructor` explícito, `@Data` no genera el constructor requerido. | Descartado | Se intentó agregar `@RequiredArgsConstructor`, pero los 3 campos de `Phone` son `@NonNull`: el constructor requerido tendría la misma firma que el de `@AllArgsConstructor` y no compila (`constructor Phone(String,String,Person) is already defined`). `@AllArgsConstructor` ya aplica los null-checks de `@NonNull`, así que el comportamiento es equivalente. Se revirtió y `Phone` queda sin cambios. |
| `pom.xml` (raíz) | Usa `maven.compiler.source/target=11`. Compilando con el JDK 17 instalado eso **no** impide usar APIs de Java 12+ (solo controla sintaxis y bytecode). | Media | Usar `maven.compiler.release=11` (en lugar de source/target), que valida contra la API de Java 11. |
| `pom.xml` (raíz) | Spring Boot 2.7.11 / Spring Framework 5.3.27 (parches viejos de la misma línea). | Baja | Subir solo de parche: Spring Boot 2.7.18 y Spring Framework 5.3.31 (la versión que trae Boot 2.7.18). Nunca Boot 3 ni target 17. |
| `application/...` | Solo existe el caso de uso de Persona; faltan puertos y casos de uso de Profesión, Estudio y Teléfono. | Alta (funcional) | Crear `ProfessionInputPort/OutputPort/UseCase`, `StudyInputPort/OutputPort/UseCase`, `PhoneInputPort/OutputPort/UseCase` siguiendo el patrón de Persona. |
| `application/pom.xml` | No hay dependencias de pruebas ni pruebas unitarias. | Media | `spring-boot-starter-test` (scope `test`) y pruebas JUnit 5 + Mockito de los 4 casos de uso. |
| `domain/.../Gender.java` | `MALE, FEMALE, OTHER` vs columna `genero ENUM('M','F')`: `OTHER` no tiene representación en BD (los mappers lo convierten a `' '`). | Info | **No se cambia** (acuerdo del equipo). El mapeo a `'M'/'F'` es responsabilidad de los adaptadores de salida; se deja anotado para ellos. |
| `pom.xml` (raíz) → `domain` | `domain` hereda del pom raíz `lombok` (provided) y `log4j-to-slf4j`. No es Spring ni otro módulo, pero `log4j-to-slf4j` no lo usa el dominio. | Info | Se deja igual: moverlo afectaría a todos los módulos (cambio no quirúrgico). `domain` no importa nada de Spring (verificado con grep). |
| `common/...` | Anotaciones `@Adapter/@Mapper/@UseCase/@Port` y excepciones correctas. | — | Sin cambios. |

## Uso del núcleo por los adaptadores (solo lectura)

- REST y CLI construyen `new PersonUseCase(personOutputPortX)` y usan `findAll/create`; no se rompe nada
  al cambiar `edit`.
- Los mappers usan `new Person()`, `new Phone()`, `new Study()`, `new Profession()` + setters: siguen
  existiendo (`@NoArgsConstructor` se mantiene).
- Las apps (`PersonAppRestApi`, `PersonAppCli`) escanean `co.edu.javeriana.as.personapp`, por lo que todo
  `@UseCase` del módulo `application` se registra como bean. Los nuevos casos de uso necesitarán los beans
  `professionOutputAdapterMaria`, `studyOutputAdapterMaria` y `phoneOutputAdapterMaria` para que las apps
  arranquen (ver README).
