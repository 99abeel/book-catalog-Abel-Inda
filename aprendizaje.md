# Registro de aprendizaje del grupo

## Entrega 1 — Echo text

- Comportamiento que añadimos o contrato que evolucionamos: Entender el código base que nos viene en la plantilla. Es un controlador simple (`BookController`) que solo devuelve texto plano con los cinco métodos básicos (GET, POST, PUT, DELETE).
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Como este código ya venía hecho de serie en la plantilla, al pasar `mvnw.cmd test` los 5 tests de `BookCheckpoint01Test` ya salían directamente en verde (no lo contamos como TDD propio).
- Cambio mínimo y resultado verde: Comprobar que compila bien con Java 25 y que los tests pasan todos sin fallos.
- Refactorización: No hace falta cambiar nada del código en esta entrega.
- Decisiones sobre capas y contrato HTTP: De momento solo tenemos el controlador `BookController`. Todo devuelve texto plano (`produces = MediaType.TEXT_PLAIN_VALUE`) y responde 200 OK. Todavía no hay base de datos ni modelos ni DTOs.
- Contribuciones, revisión y siguiente paso: Abel e Inda miramos cómo funciona el controlador y los tests de MockMvc. El siguiente paso será la Entrega 2 para meter JSON y guardar libros en memoria.
