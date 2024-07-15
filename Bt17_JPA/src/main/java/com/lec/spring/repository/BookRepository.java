package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.dto.BookNameAndCategory1;
import com.lec.spring.repository.dto.BookNameAndCategory2;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 다음과 같은 쿼리 메소드를 만들어 보자
    //    Category 는 null이면서
    //    Name 은 name
    //    CreatedAt 은 createdAt 보다 같거나 크고
    //    UpdatedAt 은 updatedAt 보다 같거나 크고
//    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual()
    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(String name, LocalDateTime createdAt, LocalDateTime updatedAt);
    // 딱 봐도, 정말 읽기 힘든 메소드가 탄생한다.  동작은 하는지 테스트 해보자

    // positional parameter 사용
    @Query(value = """
            select b from Book b
            where name = ?1 and createdAt >= ?2 and updatedAt >= ?3 and category is null
            """)
    List<Book> findByNameRecently(String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    // named parameter 사용
    @Query(value = """
            select b from Book b
            where name = :name and createdAt >= :createdAt and updatedAt >= :updatedAt and category is null
            """)
    List<Book> findByNameRecently2(
            @Param("name") String name
            , @Param("createdAt") LocalDateTime createdAt
            , @Param("updatedAt") LocalDateTime updatedAt);


    @Query(value = """
                select b.name as anme, b.category as category from Book b
                """)
    List<Tuple> findBookNameAndCategory1();

    @Query(value = "select b.name as name, b.category as category from Book b")
    List<BookNameAndCategory1> findBookNameAndCategory2();

    @Query(value = """
                select
                    new com.lec.spring.repository.dto.BookNameAndCategory2(b.name, b.category)
                from Book b
                """)
    List<BookNameAndCategory2> findBookNameAndCategory3();


    @Query(value = """
            select
                new com.lec.spring.repository.dto.BookNameAndCategory2(b.name, b.category)
            from Book b
            """)
    Page<BookNameAndCategory2> findBookNameAndCategory4(Pageable pageable);
}
