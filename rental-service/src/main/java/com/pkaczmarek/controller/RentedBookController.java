package com.pkaczmarek.controller;

import com.pkaczmarek.dto.RentedBookDto;
import com.pkaczmarek.service.RentedBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/book")
public class RentedBookController {
    private final RentedBookService rentedBookService;

    public RentedBookController(RentedBookService rentedBookService) {
        this.rentedBookService = rentedBookService;
    }

    @Operation(summary = "Get all rented books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all rented books successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentedBookDto.class)))
    })
    @GetMapping("/rented")
    @ResponseStatus(HttpStatus.OK)
    public List<RentedBookDto> getAllRentedBooks() {
        return rentedBookService.getAllRentedBooks();
    }

    @Operation(summary = "Get a rented book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rented book found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentedBookDto.class))),
            @ApiResponse(responseCode = "404", description = "Rented book not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/rented-book")
    @ResponseStatus(HttpStatus.OK)
    public RentedBookDto getBookByIsbn(@RequestParam String isbn) {
        return rentedBookService.findByIsbn(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with ISBN: " + isbn));
    }
}
