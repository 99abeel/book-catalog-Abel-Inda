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

## Entrega 3 — ResponseEntity

- Comportamiento que añadimos o contrato que evolucionamos: Expresar con exactitud los códigos y cabeceras HTTP usando `ResponseEntity`. El POST ahora responde 201 Created con la cabecera `Location` hacia el recurso creado (`/books/{id}`), y el DELETE responde 204 No Content sin cuerpo.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Al activar la semilla 3 y ejecutar los tests, vimos el rojo en `BookCheckpoint03Test`: fallaba porque esperaba código 201 en la creación y recibía 200 (`Status expected:<201> but was:<200>`), y además faltaba la cabecera `Location`.
- Cambio mínimo y resultado verde: Envolvemos los retornos de `BookController` con `ResponseEntity`. Usamos `ResponseEntity.created(URI.create("/books/" + id)).body(...)` en el POST y `ResponseEntity.noContent().build()` en el DELETE. Al pasar de nuevo los tests, la suite quedó en verde.
- Refactorización: Homogeneizamos todo el controlador para devolver `ResponseEntity` en todos los métodos (`ok()`, `created()`, `notFound()`, `noContent()`).
- Decisiones sobre capas y contrato HTTP: Los códigos HTTP y las cabeceras pertenecen exclusivamente a la capa web (`Controller`). Cuando en la entrega 4 se extraiga la capa de servicio, el servicio no debe conocer nada de `ResponseEntity` ni de `Location`.
- Contribuciones, revisión y siguiente paso: Abel completa las entregas 1, 2 y 3. Con esto queda terminada mi parte del trabajo asignado. El siguiente paso le corresponde a mi compañero Inda para la Entrega 4 (extraer la lógica a `BookService`).

## Entrega 4 — Servicio y delegación

- Comportamiento que añadimos o contrato que evolucionamos: Extraemos la lógica de negocio del controlador a `com.example.catalog.service.BookService`. El controlador pasa a delegar por constructor y el servicio se encarga del almacenamiento en memoria, la generación de ids y la conversión de `Book` a `BookResponseDTO`.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Al activar la semilla 4, el primer rojo era de compilación porque `BookCheckpoint04Test` importaba `BookService` y el proyecto todavía no tenía esa clase. Creamos la estructura mínima y después comprobamos el comportamiento: el controlador debía usar el id devuelto por el mock para construir `Location: /books/17`.
- Cambio mínimo y resultado verde: Movimos el `ConcurrentHashMap` y el `AtomicLong` a `BookService`, añadimos los métodos `list`, `get`, `create`, `update` y `delete`, e inyectamos el servicio en `BookController`. La semilla 4 y la suite completa quedaron en verde.
- Refactorización: Añadimos `BookServiceTest` para probar el servicio real sin mocks: creación con id generado, listado, actualización conservando el id y borrado. También ordenamos el listado por id para que el resultado sea determinista.
- Decisiones sobre capas y contrato HTTP: El servicio devuelve DTOs y no conoce tipos HTTP. El controlador sigue siendo el único responsable de `ResponseEntity`, de `201 Created` y de la cabecera `Location`. No se introduce JPA ni base de datos: el almacenamiento sigue en memoria.
- Contribuciones, revisión y siguiente paso: Inda implementa la entrega 4. Siguiente paso: entrega 5, definir excepciones propias en el servicio y mapearlas localmente a `404` y `400`.

## Entrega 5 — Excepciones propias y mapeo local

- Comportamiento que añadimos o contrato que evolucionamos: El servicio deja de devolver `null` o de ignorar operaciones sobre libros inexistentes. Añadimos `BookNotFoundException` para recursos que no existen y `InvalidBookException` para datos rechazados, y el controlador las traduce a `404` y `400`.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Al activar la semilla 5, el rojo era de compilación porque faltaban las dos excepciones importadas por `BookCheckpoint05Test`. Tras crear las clases y lanzarlas desde el servicio, el rojo de comportamiento se centró en el mapeo: `get` y `delete` debían devolver `404`, y `create` debía devolver `400`.
- Cambio mínimo y resultado verde: Añadimos las excepciones, hicimos que `get`, `update` y `delete` comprobaran la existencia del libro, validamos los datos en `create` y `update`, y añadimos dos `@ExceptionHandler` locales en `BookController`. La semilla 5 y la suite quedaron en verde.
- Refactorización: Ampliamos `BookServiceTest` con casos negativos: lectura, actualización y borrado de un id inexistente, y rechazo de título vacío o año negativo. Así se prueba el servicio real y no solo la reacción del controlador ante un mock.
- Decisiones sobre capas y contrato HTTP: Las excepciones son de aplicación y no contienen tipos HTTP. El servicio decide cuándo se produce el error de negocio; el controlador decide el código de estado. No usamos `404` para todos los fallos ni devolvemos `null` como sustituto de una excepción específica.
- Contribuciones, revisión y siguiente paso: Inda implementa la entrega 5. Siguiente paso: entrega 6, centralizar el mapeo en un `@RestControllerAdvice` y devolver un cuerpo de error uniforme.

## Entrega 6 — Advice global y error uniforme

- Comportamiento que añadimos o contrato que evolucionamos: Centralizamos el manejo de errores en `GlobalExceptionHandler` con `@RestControllerAdvice` y devolvemos un cuerpo uniforme con `error`, `message` y `timestamp`. También activamos `@Valid` y las restricciones `@NotBlank` y `@Min(0)` en `BookRequestDTO`.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Al activar la semilla 6, el rojo era de compilación porque faltaba `GlobalExceptionHandler`. Después, el rojo de comportamiento apareció en los casos de validación, JSON ilegible y error inesperado: el controlador todavía devolvía respuestas sin el cuerpo uniforme o dejaba escapar la excepción genérica.
- Cambio mínimo y resultado verde: Creamos `ApiError`, el advice con handlers para `BookNotFoundException`, `InvalidBookException`, `MethodArgumentNotValidException`, `HttpMessageNotReadableException` y `Exception`, añadimos `@Valid` en crear y actualizar y retiramos los handlers locales del controlador. La semilla 6 y la suite completa quedaron en verde.
- Refactorización: Añadimos `BookApiIntegrationTest` para probar el flujo real contra el servicio y el almacenamiento: crear, consultar, actualizar, borrar y comprobar los errores `NOT_FOUND`, `VALIDATION_ERROR` y `BAD_REQUEST`. Así la entrega no depende solo de un servicio mockeado.
- Decisiones sobre capas y contrato HTTP: El advice es la única capa que traduce excepciones a HTTP. Los errores esperables conservan un mensaje útil; el error inesperado responde `500 INTERNAL_ERROR` con `Unexpected error` para no filtrar detalles internos. El timestamp se genera en cada respuesta.
- Contribuciones, revisión y siguiente paso: Inda implementa la entrega 6. La práctica queda con las seis entregas, los tests semilla verdes y pruebas propias del servicio y del CRUD real. Queda la revisión cruzada del compañero y la entrega del enlace en Moodle.
