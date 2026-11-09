package com.example.catalog.service;

import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import com.example.catalog.model.Book;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<BookResponseDTO> list() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(this::toResponseDTO)
                .toList();
    }

    public BookResponseDTO get(Long id) {
        return toResponseDTO(find(id));
    }

    public BookResponseDTO create(BookRequestDTO request) {
        validate(request);
        long id = idGenerator.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponseDTO(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        validate(request);
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("Book not found with id " + id);
        }
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponseDTO(book);
    }

    public void delete(Long id) {
        if (books.remove(id) == null) {
            throw new BookNotFoundException("Book not found with id " + id);
        }
    }

    private Book find(Long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book not found with id " + id);
        }
        return book;
    }

    private void validate(BookRequestDTO request) {
        if (request == null
                || isBlank(request.isbn())
                || isBlank(request.title())
                || isBlank(request.author())
                || request.year() < 0) {
            throw new InvalidBookException("Book data is not valid");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private BookResponseDTO toResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear()
        );
    }
}
