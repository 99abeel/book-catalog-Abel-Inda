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
        Book book = books.get(id);
        return book == null ? null : toResponseDTO(book);
    }

    public BookResponseDTO create(BookRequestDTO request) {
        long id = idGenerator.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponseDTO(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        books.put(id, book);
        return toResponseDTO(book);
    }

    public void delete(Long id) {
        books.remove(id);
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
