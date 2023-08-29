package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import com.example.book.exception.ResourceNotFoundException;
import com.example.book.mapper.BookMapper;
import com.example.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book -> bookMapper.bookEntitytoDto(book)).toList();
    }

    public List<BookDto> findByName(String name){
        return bookRepository.findByNameContainingIgnoreCase(name).stream().map(book -> bookMapper.bookEntitytoDto(book)).toList();
    }

    public List<BookDto> findBooksByPriceRange(Integer minPrice,Integer maxPrice){
        return bookRepository.findByPriceBetween(minPrice, maxPrice).stream().map(book -> bookMapper.bookEntitytoDto(book)).toList();
    }

    public BookDto addBook(BookDto bookDto) {
        Book bookToSave = bookMapper.bookDtotoEntity(bookDto);
        Book savedBook = bookRepository.save(bookToSave);
        return bookMapper.bookEntitytoDto(savedBook);
    }

    public BookDto updateBook(BookDto bookDto, Integer id) {
        Book booktoUpdate = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Such Book found with that id"));
        booktoUpdate.setName(bookDto.getName());
        booktoUpdate.setPrice(bookDto.getPrice());
        Book updatedBook = bookRepository.save(booktoUpdate);
        return bookMapper.bookEntitytoDto(updatedBook);
    }

    public String deleteBook(Integer id) {
        Book bookToDelete = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such book found with Id"));
        bookRepository.delete(bookToDelete);
        return "Book delete successfully with Id " + id;
    }
}
