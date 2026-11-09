package com.example.catalog;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.BookService;
import com.example.catalog.service.InvalidBookException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookServiceTest {

    private final BookService service = new BookService();
    private final BookRequestDTO request = new BookRequestDTO("978-1", "DDD", "Evans", 2003);

    @Test
    void createsABookWithAGeneratedIdAndStoresIt() {
        BookResponseDTO created = service.create(request);

        assertThat(created.id()).isNotNull();
        assertThat(created.isbn()).isEqualTo("978-1");
        assertThat(created.title()).isEqualTo("DDD");
        assertThat(service.get(created.id())).isEqualTo(created);
    }

    @Test
    void listsStoredBooks() {
        BookResponseDTO first = service.create(request);
        BookResponseDTO second = service.create(new BookRequestDTO("978-2", "TDD", "Beck", 2002));

        assertThat(service.list()).containsExactly(first, second);
    }

    @Test
    void updatesAnExistingBookKeepingItsId() {
        BookResponseDTO created = service.create(request);
        BookRequestDTO updatedRequest = new BookRequestDTO("978-1", "DDD 2", "Evans", 2004);

        BookResponseDTO updated = service.update(created.id(), updatedRequest);

        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.title()).isEqualTo("DDD 2");
        assertThat(service.get(created.id())).isEqualTo(updated);
    }

    @Test
    void deletesAnExistingBook() {
        BookResponseDTO created = service.create(request);

        service.delete(created.id());

        assertThatThrownBy(() -> service.get(created.id()))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void rejectsAnInvalidBook() {
        BookRequestDTO invalid = new BookRequestDTO("978-1", " ", "Evans", 2003);

        assertThatThrownBy(() -> service.create(invalid))
                .isInstanceOf(InvalidBookException.class)
                .hasMessage("Book data is not valid");
    }

    @Test
    void rejectsANegativeYear() {
        BookRequestDTO invalid = new BookRequestDTO("978-1", "DDD", "Evans", -1);

        assertThatThrownBy(() -> service.create(invalid))
                .isInstanceOf(InvalidBookException.class);
    }

    @Test
    void reportsAMissingBookWhenReading() {
        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void reportsAMissingBookWhenUpdating() {
        assertThatThrownBy(() -> service.update(99L, request))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void reportsAMissingBookWhenDeleting() {
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("99");
    }
}
