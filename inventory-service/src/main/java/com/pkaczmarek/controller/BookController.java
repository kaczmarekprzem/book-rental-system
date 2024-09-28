package com.pkaczmarek.controller;

import com.pkaczmarek.dto.BookCreateDto;
import com.pkaczmarek.dto.BookDto;
import com.pkaczmarek.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all books successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class)))
    })
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Add a new book to the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto addBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.addBook(bookCreateDto);
    }

    @Operation(summary = "Get a book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/isbn")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByIsbn(@RequestParam String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @Operation(summary = "Rent a book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book rented successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/rent")
    @ResponseStatus(HttpStatus.OK)
    public void rentBook(@RequestParam String isbn, @RequestParam String clientName) {
        bookService.rentBook(isbn, clientName);
    }
}
