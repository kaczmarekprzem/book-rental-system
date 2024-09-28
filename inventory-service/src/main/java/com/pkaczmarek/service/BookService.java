package com.pkaczmarek.service;

import com.pkaczmarek.dto.BookCreateDto;
import com.pkaczmarek.dto.BookDto;
import com.pkaczmarek.exception.BookNotFoundException;
import com.pkaczmarek.mapper.BookMapper;
import com.pkaczmarek.model.Book;
import com.pkaczmarek.repository.BookRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<BookDto> getAllBooks() {
        logger.info("Fetching all books from the inventory");
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto addBook(BookCreateDto bookDto) {
        logger.info("Adding a new book with ISBN: {}", bookDto.isbn());
        Book book = bookMapper.toEntity(bookDto);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    public BookDto getBookByIsbn(String isbn) {
        logger.info("Fetching book details for ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
        return bookMapper.toDto(book);
    }

    public void rentBook(String isbn, String clientName) {
        logger.info("Renting book with ISBN: {} to client: {}", isbn, clientName);
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
        book.setBorrower(clientName);
        bookRepository.save(book);
        kafkaTemplate.send("book-rented", isbn);
        logger.info("Book with ISBN: {} has been rented successfully", isbn);
    }
}