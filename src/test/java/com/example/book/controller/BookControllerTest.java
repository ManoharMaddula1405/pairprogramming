package com.example.book.controller;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import com.example.book.mapper.BookMapper;
import com.example.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<BookDto> bookDtoList = Collections.singletonList(new BookDto(6, "shakesphere3", 103));
        when(bookService.getAllBooks()).thenReturn(bookDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[0].name").value("shakesphere3"))
                .andExpect(jsonPath("$[0].price").value(103));
    }

    @Test
    void testGetAllBooksByName() throws Exception {
        String bookName = "shakesphere3";
        List<BookDto> bookDtoList = Collections.singletonList(new BookDto(6, "shakesphere3", 103));
        when(bookService.findByName(bookName)).thenReturn(bookDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/search-by-name/{name}", bookName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("shakesphere3"))
                .andExpect(jsonPath("$[0].price").value(103));
    }


    @Test
    void testSearchBooksByPriceRange() throws Exception {
        int minPrice = 100;
        int maxPrice = 200;
        List<BookDto> bookDtoList = Collections.singletonList(new BookDto(6, "Book A", 150));
        when(bookService.findBooksByPriceRange(minPrice, maxPrice)).thenReturn(bookDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/search-by-price")
                        .param("minPrice", String.valueOf(minPrice))
                        .param("maxPrice", String.valueOf(maxPrice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Book A"))
                .andExpect(jsonPath("$[0].price").value(150));
    }

    @Test
    void testAddBook() throws Exception {
        BookDto expectedBook = new BookDto(6, "Book A", 150);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBook)))
                .andExpect(status().isCreated());

        // Create an ArgumentCaptor for PhoneDto
        ArgumentCaptor<BookDto> bookDtoCaptor = ArgumentCaptor.forClass(BookDto.class);

        // Verify that the addPhone method was called with the expected argument
        verify(bookService, times(1)).addBook(bookDtoCaptor.capture());
        BookDto capturedBookDto = bookDtoCaptor.getValue();

        assertEquals(expectedBook.getName(), capturedBookDto.getName());
        assertEquals(expectedBook.getPrice(), capturedBookDto.getPrice());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateBook() throws Exception {

        BookDto expectedBook = new BookDto(6, "Book A", 150);

        ArgumentCaptor<BookDto> idCaptor = ArgumentCaptor.forClass(BookDto.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/6", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBook)))
                .andExpect(status().isOk());

        verify(bookService, times(1)).updateBook(idCaptor.capture(), 6);

    }


    @Test
    void testDeleteBook() throws Exception {
        // Prepare test data
        Integer id = 1;

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the deletePhone() method was called with the correct id
        verify(bookService, times(1)).deleteBook(id);
    }

}