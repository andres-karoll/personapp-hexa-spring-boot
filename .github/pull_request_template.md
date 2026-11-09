## Descripción

<!-- Qué cambia y por qué. Enlaza el issue o la tarea si existe. -->

## Módulos tocados

<!-- p. ej. maria-output-adapter, rest-input-adapter -->

## Checklist

- [ ] Compila: `mvn clean install` desde la raíz termina en BUILD SUCCESS.
- [ ] Pasan todas las pruebas (las existentes y las nuevas).
- [ ] Probado contra **MARIA** y contra **MONGO** (con `docker compose up -d`).
- [ ] Documentado (README, `docs/` o comentarios donde haga falta).
- [ ] No se tocaron módulos de otra persona sin avisar al equipo.
- [ ] No se cambiaron los contratos de `application` (puertos) ni el dominio sin acordarlo.
