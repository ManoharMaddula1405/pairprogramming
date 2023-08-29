package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import com.example.book.exception.ResourceNotFoundException;
import com.example.book.mapper.BookMapper;
import com.example.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1,"Book A", 100));
        bookList.add(new Book(2,"Book B", 150));

        when(bookRepository.findAll()).thenReturn(bookList);

        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1,"Book A", 100));
        bookDtoList.add(new BookDto(2,"Book B", 150));

        when(bookMapper.bookEntitytoDto(any(Book.class))).thenReturn(bookDtoList.get(0), bookDtoList.get(1));

        List<BookDto> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book A", result.get(0).getName());
        assertEquals(100, result.get(0).getPrice());
        assertEquals("Book B", result.get(1).getName());
        assertEquals(150, result.get(1).getPrice());
    }

    @Test
    void testFindByName() {
        String searchName = "Book A";

        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1,"Book A", 100));

        when(bookRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(bookList);
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(2,"Book A", 100));
        when(bookMapper.bookEntitytoDto(any(Book.class))).thenReturn(bookDtoList.get(0));
        List<BookDto> result = bookService.findByName(searchName);
        assertEquals(1, result.size());
        assertEquals("Book A", result.get(0).getName());
        assertEquals(100, result.get(0).getPrice());
    }


    @Test
    void testAddBook() {
        BookDto bookDto = new BookDto(1,"Book A", 100);
        Book bookToSave = new Book(1,"Book A", 100);

        when(bookMapper.bookDtotoEntity(bookDto)).thenReturn(bookToSave);
        when(bookRepository.save(bookToSave)).thenReturn(bookToSave);

        when(bookMapper.bookEntitytoDto(bookToSave)).thenReturn(bookDto);

        BookDto result = bookService.addBook(bookDto);

        assertEquals("Book A", result.getName());
        assertEquals(100, result.getPrice());
    }

    @Test
    void testUpdateBook() {
        int bookId = 1;
        BookDto bookDto = new BookDto(1,"Updated Book", 150);
        Book bookToUpdate = new Book(1,"Existing Book", 100);
        Book updatedBook = new Book(1,"Updated Book", 150);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToUpdate));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        when(bookMapper.bookEntitytoDto(updatedBook)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(bookDto, bookId);

        assertEquals("Updated Book", result.getName());
        assertEquals(150, result.getPrice());
    }


    @Test
    void testDeleteBook() {
        BookRepository bookRepository = mock(BookRepository.class);
        bookService.deleteBook(1);
        verify(bookRepository, times(1)).deleteById(1);
    }

}
