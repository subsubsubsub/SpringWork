package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.domain.Publisher;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import com.lec.spring.repository.dto.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void bookTest() {
        System.out.println("\n-- TEST#bookTest() ---------------------------------------------");
        Book book = new Book();
        book.setName("JPA 스터디");
        book.setAuthorId(1L);
//        book.setPublisherId(1L);
        bookRepository.save(book);  // INSERT

        System.out.println(bookRepository.findAll());
        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    @Transactional
        // 지금은 OneToMany 에서 발생하는 LazyInitializationException 을 막기위해 사용
    void bookRelationTest() {
        System.out.println("\n-- TEST#bookRelationTest() ---------------------------------------------");

        // 테스트용 데이터 입력
        givenBookAndReview();

        // 특정 User 가져오기
        User user = userRepository.findByEmail("martin@redknight.com");

        // 특정 User 가 남긴 review 정보들 가져오기
        System.out.println("Review : " + user.getReviews());

        // 특정 User 가 남긴 review 중 첫 번째 Review 의 Book 정보 가져오기
        System.out.println("Book : " + user.getReviews().get(0).getBook());

        // 특정 User 가 남긴 review 중 첫 번째 Review 의 Book 의 Publisher 정보 가져오기
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());


        System.out.println("\n------------------------------------------------------------\n");
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("JPA 완전정복");
        book.setPublisher(publisher);   // FK 설정

        return bookRepository.save(book);
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("K-출판사");

        return publisherRepository.save(publisher);
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("내 인생을 바꾼 책");
        review.setContent("너무너무 재미있고 즐거운 책이었어요");
        review.setScore(5.0f);
        review.setUser(user);   // FK
        review.setBook(book);   // FK

        reviewRepository.save(review);
    }

    private User givenUser() {
        // 1번 User값 리턴
        return userRepository.findByEmail("martin@redknight.com");
    }

    private void givenBookAndReview() {
        // 리뷰 저장
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    //  --------------------------------------------------------------------
    // 커스텀 쿼리
    @Test
    void queryTest1() {
        System.out.println("findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual() : ");
        System.out.println(bookRepository.findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(
                "JPA 완전정복",
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().minusDays(1L)
        ));
    }

    @Test
    void queryTest2() {
        System.out.println("findByNameRecently: " +
                bookRepository.findByNameRecently(
                        "JPA 완전정복"
                        , LocalDateTime.now().minusDays(1L)
                        , LocalDateTime.now().minusDays(1L)));
    }

    @Test
    void queryTest3() {
        System.out.println("findByNameRecently: " +
                bookRepository.findByNameRecently2(
                        "JPA 완전정복"
                        , LocalDateTime.now().minusDays(1L)
                        , LocalDateTime.now().minusDays(1L)));
    }

    @Test
    void queryTest4() {
        bookRepository.findBookNameAndCategory1().forEach(tuple -> {
            System.out.println(tuple.get(0) + " : " + tuple.get(1));
        });
    }

    @Test
    void queryTest5() {
        bookRepository.findBookNameAndCategory2().forEach(b -> {
            System.out.println(b.getName() + " : " + b.getCategory());
        });
    }

    @Test
    void queryTest6() {
        bookRepository.findBookNameAndCategory3().forEach(b -> {
            System.out.println(b.getName() + " : " + b.getCategory());
        });
    }

    @Test
    void queryTest7() {
        bookRepository.findBookNameAndCategory4(PageRequest.of(0, 1)).forEach(b -> {
            System.out.println(b.getName() + " : " + b.getCategory());
        });
    }

    @Test
    void nativeQueryTest1() {
        System.out.println("💵".repeat(30));
        bookRepository.findAll().forEach(System.out::println);

        System.out.println("💵".repeat(30));
        bookRepository.findAllCustom1().forEach(System.out::println);
    }

    @Test
    void nativeQueryTest2() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            book.setCategory("IT전문서");
        }
        System.out.println("👻".repeat(30));     // SELECT * 3 + UPDATE * 3
        bookRepository.saveAll(books);

        bookRepository.findAll().forEach(System.out::println);
    }

    @Test
    void nativeQueryTest3() {
        System.out.println("affected rows: " + bookRepository.updateCategories());
        System.out.println("🦀".repeat(30));
        System.out.println(bookRepository.findAllCustom1());
    }

    @Test
    void nativeQueryTest4(){
        System.out.println(bookRepository.showTables());
    }

    // --------------------------------------------------------------
    // Converter
    @Test
    void converterrTest1() {
        bookRepository.findAll().forEach(System.out::println);
    }

    @Test
    void converterTest2() {
        Book book = new Book();
        book.setName("냥바닥 키보드");
        book.setStatus(new BookStatus(200));

    // ↓ Converter 에 의해 BookStatus 는 DB interger  로 변환하여 저장된다.
        bookRepository.save(book);  // INSERT

        System.out.println(bookRepository.findRowReord().entrySet());
    }

    // 개발자가 @Converter 에 DB 로의 작성 부분을 빼먹은 경우는?
    @Test
    void converterTest3(){
        bookRepository.findAll().forEach(System.out::println);
        bookRepository.findAll().forEach(System.out::println);
    }



}


























