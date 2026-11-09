package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    private static final String BOOK = """
            {"isbn":"978-1","title":"DDD","author":"Evans","year":2003}
            """;

    private String createBook() throws Exception {
        return mvc.perform(post("/books").contentType("application/json").content(BOOK))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/books/")))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getHeader("Location");
    }

    @Test
    void performsTheRealCrudFlowWithTheServiceAndStorage() throws Exception {
        String location = createBook();

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDD"))
                .andExpect(jsonPath("$.author").value("Evans"));

        mvc.perform(put(location).contentType("application/json").content("""
                        {"isbn":"978-1","title":"DDD 2","author":"Evans","year":2004}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDD 2"))
                .andExpect(jsonPath("$.year").value(2004));

        mvc.perform(delete(location)).andExpect(status().isNoContent());

        mvc.perform(get(location))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message", containsString("Book not found")));
    }

    @Test
    void rejectsInvalidDataWithValidationErrorPayload() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("""
                        {"isbn":"978-1","title":" ","author":"Evans","year":2003}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void reportsMissingResourcesWithNotFoundPayload() throws Exception {
        mvc.perform(get("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").isNotEmpty());

        mvc.perform(delete("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void reportsMalformedJsonWithBadRequestPayload() throws Exception {
        mvc.perform(post("/books").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }
}
