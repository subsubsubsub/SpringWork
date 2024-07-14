package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {

    @Autowired
    private BookReviewInfoRepository bookReviewInfoRepository;

    @Autowired
    private BookRepository bookRepository;

/*
    @Test
    void crudTest1() {
        System.out.println("\n-- TEST#crudTest1() ---------------------------------------------");

        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        bookReviewInfo.setBookId(1L);
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);  // INSERT

        bookReviewInfoRepository.findAll().forEach(System.out::println);

        System.out.println("\n------------------------------------------------------------\n");
    }


    @Test
    void crudTest2() {
        System.out.println("\n-- TEST#crudTest2() ---------------------------------------------");

        // 1. 새로운 Book 생성
        Book book = new Book();
        book.setName("JPA 완전정복");
        book.setAuthorId(1L);
        book.setPublisherId(1L);

        bookRepository.save(book);  // INSERT, book 의 id 값은?    => 1L
        Long newBookId = book.getId();
        System.out.println(">>> New Book Id : " + newBookId);

        // 2. 새로운 BookReviewInfo 생성
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        bookReviewInfo.setBookId(newBookId);    // book 에 연결!!
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);  // BookReviewInfo 에 INSERT, 과연  id값은
        Long newBookReviewId = bookReviewInfo.getId();
        System.out.println(">>> New BookReviewInfo Id :" + newBookReviewId);
        System.out.println(">>> " + bookReviewInfoRepository.findAll());

        // 자식(child) 을 통한 부모(parent) 접근
        Long parentBookId = bookReviewInfoRepository.findById(newBookReviewId)      // 자식을 먼저 선택
                .orElseThrow(RuntimeException::new).getBookId();  // 부모의 id 를 얻어오기

        Book result = bookRepository.findById(parentBookId).orElseThrow(RuntimeException::new);    // 부모에 접근

        System.out.println(">>> " + result);


        System.out.println("\n------------------------------------------------------------\n");
    }
*/

    @Test
    void crudTest3() {
        System.out.println("\n-- TEST#crudTest3() ---------------------------------------------");

        givenBookReviewInfo();

        System.out.println("-".repeat(30));

        // 자식 -> 부모 조회하기
        // BookReviewInfo -> Book 조회하기
        Book result = bookReviewInfoRepository.findById(1L) // 이때 join 문으로 OneToOne 참조하는 부모까지 같이 가져온다!
                .orElseThrow(RuntimeException::new)
                .getBook(); // 그래서 부모 값을 참조하고 있다.

        System.out.println(">>> " + result);

        System.out.println("*".repeat(30));

        // 부모 -> 자식 조회하기
        // Book -> BookReviewInfo
        BookReviewInfo result2 =
                bookRepository.findById(1L).orElseThrow(RuntimeException::new).getBookReviewInfo();
        System.out.println(">>> " + result2);

        System.out.println("\n------------------------------------------------------------\n");

    }

    // Book을 생성하는 로직을 별도로 작성
    private Book givenBook() {
        // ① 새로운 Book 생성
        Book book = new Book();
        book.setName("JPA 완전정복");
        book.setAuthorId(1L);
//        book.setPublisherId(1L);

        // save() 의 리턴값은 영속화된 Entity 다.
        return bookRepository.save(book); // Book 에 INSERT
    }

    // BookReviewInfo를 생성하는 로직을 별도로 작성
    private void givenBookReviewInfo() {
        // ② 새로운 BookReviewInfo 생성
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        bookReviewInfo.setBook(givenBook()); // ★영속화된 Book 에 연결★
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);  // BookReviewInfo 에 INSERT

        System.out.println(">>> " + bookReviewInfoRepository.findAll());
    }

}





























