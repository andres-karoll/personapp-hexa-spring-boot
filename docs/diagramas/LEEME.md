# Diagramas C4 · Laboratorio 2 (PersonAPP)

Fuentes PlantUML (`.puml`) con la librería C4-PlantUML v2.10.0, renderizadas a SVG y PNG.
Para regenerar: `plantuml -tsvg -tpng <archivo>.puml` (requiere internet para descargar la librería).

| Archivo | Nivel | Qué muestra |
|---|---|---|
| 01_paisaje | Paisaje | Quién usa el sistema y qué sistemas externos intervienen |
| 02_contexto | 1 · Contexto | Operador de consola, cliente de la API y el sistema |
| 03_contenedores | 2 · Contenedores | API, CLI, MariaDB, MongoDB y mongo-init dentro de Docker Compose |
| 04_componentes | 3 · Componentes | Controladores, adaptadores, casos de uso, puertos y adaptadores de salida (hexagonal) |
| 05_codigo | 4 · Código | Clases principales del flujo de Persona |
| 06a_dinamico_crear_persona_api | Dinámico | POST /api/v1/persona en MariaDB, con validación de duplicados |
| 06b_dinamico_eliminar_persona_mongo | Dinámico | DELETE en MongoDB con borrado en cascada de estudios y teléfonos |
| 07_despliegue | Despliegue | Docker Desktop en macOS, red de Compose, contenedores y volúmenes |
| 08_arquitectura_capas | Capas | Entrada, núcleo y salida; la regla de dependencia hacia el núcleo |
