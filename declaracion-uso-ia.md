# Declaración de uso de IA

Completar por todos los integrantes, también si no se ha usado IA. No pegar
credenciales ni datos personales de terceros.

| Integrante | Herramienta/modelo o «no utilizada» | Uso concreto | Archivos/commits afectados | Cómo se verificó y qué se aprendió |
|---|---|---|---|---|
| Abel | no utilizada | Esta entrega 1 venía ya hecha en la plantilla, solo la revisé y ejecuté los tests. | `BookController.java`, `BookCheckpoint01Test.java` (Entrega 1) | Pasé `mvnw.cmd test` en la terminal y comprobé que los 5 tests de eco salían en verde. |
| Abel | Gemini | Preguntar cómo recibir y devolver JSON con DTOs en vez de usar la entidad directamente, y cómo guardar en un Map con id autoincremental. | `BookController.java`, `BookRequestDTO.java`, `BookResponseDTO.java`, `Book.java` (Entrega 2) | Con `mvnw.cmd test` en verde. Aprendí a usar records para los DTOs y que Spring convierte el cuerpo JSON con `@RequestBody`. |
| Abel | Gemini | Consultar el fallo 406 Not Acceptable que salía en el test `startsWithEmptyJsonList`. | `BookController.java` (Entrega 2) | Vi en el error que el test pedía JSON y el controlador tenía puesto texto plano fijo; se solucionó quitando el `produces` de la anotación. |
| Abel | Gemini | Consultar cómo construir la respuesta 201 Created con la cabecera Location usando `ResponseEntity.created(URI)` y cómo devolver 204 sin cuerpo en delete. | `BookController.java` (Entrega 3) | Pasando los tests de `BookCheckpoint03Test` en verde. Aprendí que `URI.create("/books/" + id)` fija la cabecera Location y que `noContent().build()` no lleva cuerpo. |

Cada integrante debe comprender y poder defender sus cambios. La IA puede
ayudar a interpretar errores y conceptos; no sustituye la autoría ni justifica
copiar una solución completa sin entenderla.
