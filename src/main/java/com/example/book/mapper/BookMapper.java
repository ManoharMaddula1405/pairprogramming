package com.example.book.mapper;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    @Autowired
    private final ModelMapper modelMapper;

    public BookDto bookEntitytoDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    public Book bookDtotoEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }


}
