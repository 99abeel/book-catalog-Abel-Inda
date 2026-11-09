# Declaración de uso de IA

Completar por todos los integrantes, también si no se ha usado IA. No pegar
credenciales ni datos personales de terceros.

| Integrante | Herramienta/modelo o «no utilizada» | Uso concreto | Archivos/commits afectados | Cómo se verificó y qué se aprendió |
|---|---|---|---|---|
| Abel | no utilizada | Esta entrega 1 venía ya hecha en la plantilla, solo la revisé y ejecuté los tests. | `BookController.java`, `BookCheckpoint01Test.java` (Entrega 1) | Pasé `mvnw.cmd test` en la terminal y comprobé que los 5 tests de eco salían en verde. |
| Abel | Gemini | Preguntar cómo recibir y devolver JSON con DTOs en vez de usar la entidad directamente, y cómo guardar en un Map con id autoincremental. | `BookController.java`, `BookRequestDTO.java`, `BookResponseDTO.java`, `Book.java` (Entrega 2) | Con `mvnw.cmd test` en verde. Aprendí a usar records para los DTOs y que Spring convierte el cuerpo JSON con `@RequestBody`. |
| Abel | Gemini | Consultar el fallo 406 Not Acceptable que salía en el test `startsWithEmptyJsonList`. | `BookController.java` (Entrega 2) | Vi en el error que el test pedía JSON y el controlador tenía puesto texto plano fijo; se solucionó quitando el `produces` de la anotación. |
| Abel | Gemini | Consultar cómo construir la respuesta 201 Created con la cabecera Location usando `ResponseEntity.created(URI)` y cómo devolver 204 sin cuerpo en delete. | `BookController.java` (Entrega 3) | Pasando los tests de `BookCheckpoint03Test` en verde. Aprendí que `URI.create("/books/" + id)` fija la cabecera Location y que `noContent().build()` no lleva cuerpo. |
| Inda | Gemini | Consultas para entender cómo extraer la lógica a un servicio, qué responsabilidad debe quedar en el controlador y cómo probar el servicio real sin mocks. | `BookService.java`, `BookController.java`, `BookServiceTest.java` (Entrega 4) | Activé la semilla 4 y ejecuté `mvnw.cmd test`; comprobé que el controlador delega en el servicio y que `BookServiceTest` demuestra el almacenamiento real. |
| Inda | Gemini | Consultas sobre excepciones propias de aplicación y su mapeo local a `404` y `400`, sin llevar tipos HTTP al servicio. | `BookNotFoundException.java`, `InvalidBookException.java`, `BookService.java`, `BookController.java`, `BookServiceTest.java` (Entrega 5) | Activé la semilla 5 y ejecuté la suite; además amplié las pruebas unitarias para comprobar que el servicio lanza las excepciones en los casos reales. |
| Inda | Gemini | Consultas sobre `@RestControllerAdvice`, `@Valid`, Bean Validation y el formato uniforme de error con `error`, `message` y `timestamp`. | `GlobalExceptionHandler.java`, `ApiError.java`, `BookRequestDTO.java`, `BookController.java`, `BookApiIntegrationTest.java` (Entrega 6) | Activé la semilla 6 y ejecuté `mvnw.cmd test`; comprobé con `BookApiIntegrationTest` el CRUD real y los errores `NOT_FOUND`, `VALIDATION_ERROR` y `BAD_REQUEST`. |

Cada integrante debe comprender y poder defender sus cambios. La IA puede
ayudar a interpretar errores y conceptos; no sustituye la autoría ni justifica
copiar una solución completa sin entenderla.
