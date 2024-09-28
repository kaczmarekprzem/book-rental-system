package com.pkaczmarek.service;

import com.pkaczmarek.dto.BookCreateDto;
import com.pkaczmarek.dto.BookDto;
import com.pkaczmarek.exception.BookNotFoundException;
import com.pkaczmarek.mapper.BookMapper;
import com.pkaczmarek.model.Book;
import com.pkaczmarek.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_shouldReturnListOfBookDtos() {
        // Given
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("9780134685991");
        when(bookRepository.findAll()).thenReturn(List.of(book));

        BookDto bookDto = new BookDto("Effective Java", "Joshua Bloch", "9780134685991", "Programming", null);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        List<BookDto> result = bookService.getAllBooks();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).title());
    }

    @Test
    void addBook_shouldSaveBookAndReturnBookDto() {
        // Given
        BookCreateDto bookCreateDto = new BookCreateDto("Effective Java", "Joshua Bloch", "9780134685991");
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("9780134685991");

        BookDto bookDto = new BookDto("Effective Java", "Joshua Bloch", "9780134685991", "Programming", null);

        when(bookMapper.toEntity(bookCreateDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto result = bookService.addBook(bookCreateDto);

        // Then
        assertNotNull(result);
        assertEquals("Effective Java", result.title());
        assertEquals("9780134685991", result.isbn());
    }


    @Test
    void getBookByIsbn_shouldReturnBookDto_whenBookExists() {
        // Given
        String isbn = "9780134685991";
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn(isbn);

        BookDto bookDto = new BookDto("Effective Java", "Joshua Bloch", isbn, "Programming", null);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto result = bookService.getBookByIsbn(isbn);

        // Then
        assertNotNull(result);
        assertEquals(isbn, result.isbn());
    }

    @Test
    void getBookByIsbn_shouldThrowBookNotFoundException_whenBookDoesNotExist() {
        // Given
        String isbn = "9780134685991";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn(isbn));
    }

    @Test
    void rentBook_shouldUpdateBookBorrowerAndSendKafkaMessage() {
        // Given
        String isbn = "9780134685991";
        String clientName = "John Doe";
        Book book = new Book();
        book.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        // When
        bookService.rentBook(isbn, clientName);

        // Then
        assertEquals(clientName, book.getBorrower());
        verify(kafkaTemplate, times(1)).send("book-rented", isbn);
    }
}
