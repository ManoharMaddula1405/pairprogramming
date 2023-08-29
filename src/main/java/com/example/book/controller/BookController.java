package com.example.book.controller;

import com.example.book.dto.BookDto;
import com.example.book.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto){
        return new ResponseEntity<>(bookService.addBook(bookDto),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto,@PathVariable @Min(0)  Integer id){
        return new ResponseEntity<>(bookService.updateBook(bookDto,id),HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<List<BookDto>> getAllBooks(){
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/search-by-name/{name}")
    public ResponseEntity<List<BookDto>> getAllBooksByName(@PathVariable @NotBlank @Size(min = 1) String name){
        return new ResponseEntity<>(bookService.findByName(name), HttpStatus.OK);
    }
    @GetMapping("/search-by-price")
    public ResponseEntity<List<BookDto>> searchBooksByPriceRange(@RequestParam @Min(0) Integer minPrice, @RequestParam @Min(0) Integer maxPrice) {
        List<BookDto> matchingBooks = bookService.findBooksByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(matchingBooks, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable @Min(0) Integer id){
        return new ResponseEntity<>(bookService.deleteBook(id),HttpStatus.OK);
    }
}
