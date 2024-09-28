package com.pkaczmarek.service;

import com.pkaczmarek.dto.RentedBookDto;
import com.pkaczmarek.mapper.RentedBookMapper;
import com.pkaczmarek.model.RentedBook;
import com.pkaczmarek.repository.RentedBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Answers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentedBookServiceTest {

    @Mock
    private RentedBookRepository rentedBookRepository;

    @Mock
    private RentedBookMapper rentedBookMapper;

    @Mock(answer = Answers.RETURNS_SELF)
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private RentedBookService rentedBookService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testFindByIsbn_Success() {
        // Given
        String isbn = "1234567890123";
        RentedBook rentedBook = new RentedBook();
        rentedBook.setIsbn(isbn);
        RentedBookDto rentedBookDto = new RentedBookDto(1L, "Title", "Author", isbn, null);

        when(rentedBookRepository.findByIsbn(isbn)).thenReturn(Optional.of(rentedBook));
        when(rentedBookMapper.toDto(rentedBook)).thenReturn(rentedBookDto);

        // When
        Optional<RentedBookDto> result = rentedBookService.findByIsbn(isbn);

        // Then
        assertTrue(result.isPresent());
        assertEquals(isbn, result.get().isbn());
        verify(rentedBookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void testFindByIsbn_NotFound() {
        // Given
        String isbn = "1234567890123";
        when(rentedBookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // When
        Optional<RentedBookDto> result = rentedBookService.findByIsbn(isbn);

        // Then
        assertFalse(result.isPresent());
        verify(rentedBookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void testGetAllRentedBooks() {
        // Given
        RentedBook rentedBook1 = new RentedBook();
        rentedBook1.setIsbn("1234567890123");
        RentedBook rentedBook2 = new RentedBook();
        rentedBook2.setIsbn("0987654321098");

        RentedBookDto rentedBookDto1 = new RentedBookDto(1L, "Title1", "Author1", "1234567890123", null);
        RentedBookDto rentedBookDto2 = new RentedBookDto(2L, "Title2", "Author2", "0987654321098", null);

        when(rentedBookRepository.findAll()).thenReturn(List.of(rentedBook1, rentedBook2));
        when(rentedBookMapper.toDto(rentedBook1)).thenReturn(rentedBookDto1);
        when(rentedBookMapper.toDto(rentedBook2)).thenReturn(rentedBookDto2);

        // When
        List<RentedBookDto> result = rentedBookService.getAllRentedBooks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rentedBookRepository, times(1)).findAll();
    }
}
