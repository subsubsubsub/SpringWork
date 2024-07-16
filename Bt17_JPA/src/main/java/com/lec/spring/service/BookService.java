package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // @Converter 에서 '조회' 부분 로직을 만들지 않은 경우.
    @Transactional
    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();
        books.forEach(System.out::println);
        return books;
    }
}
