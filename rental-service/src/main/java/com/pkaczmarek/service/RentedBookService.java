package com.pkaczmarek.service;

import com.pkaczmarek.dto.RentedBookDto;
import com.pkaczmarek.mapper.RentedBookMapper;
import com.pkaczmarek.model.RentedBook;
import com.pkaczmarek.repository.RentedBookRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentedBookService {
    private static final Logger logger = LoggerFactory.getLogger(RentedBookService.class);

    private final RentedBookRepository rentedBookRepository;
    private final WebClient webClient;
    private final RentedBookMapper rentedBookMapper;

    public RentedBookService(RentedBookRepository rentedBookRepository, WebClient.Builder webClientBuilder, RentedBookMapper rentedBookMapper) {
        this.rentedBookRepository = rentedBookRepository;
        this.webClient = webClientBuilder.baseUrl("http://inventory-service:8080").build();
        this.rentedBookMapper = rentedBookMapper;
    }

    public Optional<RentedBookDto> findByIsbn(String isbn) {
        Optional<RentedBook> rentedBook = rentedBookRepository.findByIsbn(isbn);
        return rentedBook.map(rentedBookMapper::toDto);
    }

    public List<RentedBookDto> getAllRentedBooks() {
        logger.info("Fetching all rented books.");
        return rentedBookRepository.findAll().stream()
                .map(rentedBookMapper::toDto)
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "book-rented", groupId = "rental-service-group")
    @Transactional
    public void addRentedBook(String isbn) {
        try {
            logger.info("Fetching book information for ISBN: {}", isbn);
            RentedBook rentedBook = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/book/isbn")
                            .queryParam("isbn", isbn)
                            .build())
                    .retrieve()
                    .bodyToMono(RentedBook.class)
                    .block();

            if (rentedBook != null) {
                rentedBookRepository.save(rentedBook);
                logger.info("Book with ISBN: {} has been added to rented books.", isbn);
            } else {
                throw new RuntimeException("Book not found in InventoryService.");
            }
        } catch (WebClientResponseException ex) {
            logger.error("Failed to retrieve book information from InventoryService: {}", ex.getMessage());
            throw new RuntimeException("Failed to retrieve book information from InventoryService: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("An error occurred while processing the rented book: {}", ex.getMessage());
            throw new RuntimeException("An error occurred while processing the rented book: " + ex.getMessage(), ex);
        }
    }
}
