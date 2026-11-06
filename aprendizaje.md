# Registro de aprendizaje del grupo

## Entrega 1 — Echo text

- Comportamiento que añadimos o contrato que evolucionamos: Entender el código base que nos viene en la plantilla. Es un controlador simple (`BookController`) que solo devuelve texto plano con los cinco métodos básicos (GET, POST, PUT, DELETE).
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Como este código ya venía hecho de serie en la plantilla, al pasar `mvnw.cmd test` los 5 tests de `BookCheckpoint01Test` ya salían directamente en verde (no lo contamos como TDD propio).
- Cambio mínimo y resultado verde: Comprobar que compila bien con Java 25 y que los tests pasan todos sin fallos.
- Refactorización: No hace falta cambiar nada del código en esta entrega.
- Decisiones sobre capas y contrato HTTP: De momento solo tenemos el controlador `BookController`. Todo devuelve texto plano (`produces = MediaType.TEXT_PLAIN_VALUE`) y responde 200 OK. Todavía no hay base de datos ni modelos ni DTOs.
- Contribuciones, revisión y siguiente paso: Abel e Inda miramos cómo funciona el controlador y los tests de MockMvc. El siguiente paso será la Entrega 2 para meter JSON y guardar libros en memoria.

## Entrega 2 — CRUD JSON en memoria

- Comportamiento que añadimos o contrato que evolucionamos: Pasamos de responder texto plano a manejar JSON en la API. Implementamos las operaciones CRUD completas (crear, leer todos, leer uno por id, actualizar y borrar) guardando los libros en memoria.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Al activar la entrega 2 y pasar `mvnw.cmd test`, vimos el rojo en `BookCheckpoint02Test`: fallaba con `Content type [text/plain] is not compatible with [application/json]` y con error `406 Not Acceptable`, porque el controlador viejo obligaba a devolver texto plano en lugar de JSON.
- Cambio mínimo y resultado verde: Creamos los DTOs `BookRequestDTO` y `BookResponseDTO` con records, la clase `Book` en `model`, y en `BookController` quitamos el `produces` de texto plano y metimos un `ConcurrentHashMap` con `AtomicLong`. Al volver a ejecutar los tests, todos salieron en verde.
- Refactorización: Hicimos un método auxiliar `toResponseDTO(Book)` en el controlador para no repetir la conversión del modelo al DTO en cada endpoint.
- Decisiones sobre capas y contrato HTTP: Mantenemos el CRUD dentro de `BookController` en esta fase. Usamos DTOs distintos para la entrada (sin id) y la salida (con id autogenerado). El código de creación `POST` sigue devolviendo 200 OK según marca el contrato de esta entrega.
- Contribuciones, revisión y siguiente paso: Abel implementa el modelo, los DTOs y el controlador JSON. Siguiente paso: Entrega 3 para ajustar los códigos HTTP usando `ResponseEntity` (201 Created con cabecera Location y 204 No Content para borrado).
