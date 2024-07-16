package com.lec.spring.repository;

// Repository 생성
// JpaRepository<Entity타입, Id타입> 상속 ← 바로 이게 Spring Data JPA 가 지원해주는 영역!
//   상속받은 것만으로도 많은 JPA 메소드를 편리하게 사용 가능하게 된다.
//   상속받은 것만으로도 이미 Spring Context 에 생성된다. → 주입 가능!


import com.lec.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1.
//    User findByName(String name);   // 단일 객체를 리턴하는 경우에만 가능
//    List<User> findByName(String username); // 여러객체가 select 되는 경우
    Optional<User> findByName(String name);

    // 2.
    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    User findUsersByEmail(String email);

    // 3. find아무말... 가능
    User findYouinahByEmail(String email);

    // 4. 잘못된 네이밍
//    User findByByName(String name); // <- 'ByName' 이라는 property 를 찾는다! -> 가동 자체가 안된다!

    // 5. First, Top
    List<User> findFirst1Byname(String name);

    List<User> findFirst2ByName(String name);

    List<User> findTop1ByName(String name);

    List<User> findTop2ByName(String name);


    // 6. Last 는 없는 키워드!    find 와 By 사이의 키워드 아닌것들은 무시된다.
    List<User> findLast1ByName(String name);    // 그냥 findByName() 과 동일한 결과.

    // 7. And, Or
    List<User> findByEmailAndName(String email, String name);

    List<User> findByEmailOrName(String email, String name);


    // 8. After, Before
    List<User> findByCreatedAtAfter(LocalDateTime dateTime);

    List<User> findByIdAfter(Long id);  // Id 값이 주어진 매개변수 보다 큰 값들

    List<User> findByNameBefore(String name); // name 값이 주어진 매개변수보다 사전 순으로 작은것들


    // 9. GreaterThan, GreaterThanEqual, LessThan, LessThanEqual
    //  >, >=, <, <=
    List<User> findByCreatedAtGreaterThan(LocalDateTime dateTime);  // 매개변수로 주어진 dataTime 보다 이후 시간

    List<User> findByNameGreaterThanEqual(String name);

    // 10. Between
    List<User> findByCreatedAtBetween(LocalDateTime dateTime1, LocalDateTime dateTime2);
    // dateTime1 과 같거나 크고 dateTime2 보다 같거나 작은...

    List<User> findByIdBetween(Long id1, Long id2);

    // k BETWEEN a AND b    =>     a <= k AND k <= b
    List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);

    // 11. Null, Empty
    List<User> findByIdIsNotNull();

//    List<User> findByIdIsNotEmpty();    // 스프링 가동시 에러
    // IsEmpty / IsNotEmpty can only be used on collection properties
    // Query method 의 Empty 는 문자열의 Empty 와 다르다!
    // Query method 의 Empty 는 collection 에서의 Empty, not empty 를 체크한다.

//    List<User> findByAddressIsNotEmpty();

    // 12. In, NotIn
    List<User> findByNameIn(List<String> names);

    // 13. StartingWith, EndingWith, Contains
    // 문자열에 대한 검색 쿼리, LIKE 사용
    List<User> findByNameStartingWith(String name);

    List<User> findByNameEndingWith(String name);

    List<User> findByEmailContains(String email);

    // 14. Like
    List<User> findByEmailLike(String email);

    // 15. Is, Equals
    // 특별한 역할 안함. 생략가능.     가독성 차원에서 남겨진 키워드들.
    Set<User> findByNameIs(String name);    // findByName 과 동일하게 동작
    // 아래 4개는 동일하게 동작하는 메소드다.
    // Set<User> findByName(String name);
    // Set<User> findUserByName(String name);
    // Set<User> findUserByNameIs(String name);
    // Set<User> findUserByNameEquals(String name);

    // 16. OrderBy
    List<User> findTopByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDesc(String name);

    // 17. 정렬기준 추가
    List<User> findFirstByNameOrderByIdDescEmailDesc(String name);

    // 18. Sort 객체를 매개변수로 사용한 정렬
    List<User> findFirstByName(String name, Sort sort);

    // 19. Paging
    Page<User> findByName(String name, Pageable pageable);

    // 20. Enum 처리
    @Query(value = "SELECT * FROM t_user LIMIT 1", nativeQuery = true)
    Map<String, Object> findRowRecord();

    // Embed
    // 두개의 Address 값에 null 이나 empty 가 저장되면 DB 에는 무엇이 저장되어 있을까?
    @Query(value = "SELECT * FROM t_user", nativeQuery = true)
    List<Map<String, Object>> findAllRowRecord();


}


