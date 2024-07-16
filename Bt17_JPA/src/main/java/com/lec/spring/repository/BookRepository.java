package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.dto.BookNameAndCategory1;
import com.lec.spring.repository.dto.BookNameAndCategory2;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // Native Query
    @Query(value = "select * from book", nativeQuery = true)
    List<Book> findAllCustom1();

    @Transactional      // update / delete / insert 수행하는 native query 에 필요!!
    @Modifying
    @Query(value = "update book set category = 'IT전문서'", nativeQuery = true)
    int updateCategories();
    // DML 의 경우 리턴타입이 void, int, long 일수 있다.
    // int 나 long 리턴하게 되면 affected row 를 받게 된다.

    // native query 로 JPA 에 없는 쿼리 하기
    @Query(value = "show tables", nativeQuery = true)
    List<String> showTables();

    // Converter
    // 실제 변환되어 저장되는 status 값 확인을 위한 native query 생성
    // 가장 마지막에 저장된 book 정보만 읽어오기
    @Query(value = "select * from book order by id desc limit 1", nativeQuery = true)
    Map<String, Object> findRowReord();

}
