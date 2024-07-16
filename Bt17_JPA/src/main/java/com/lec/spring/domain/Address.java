package com.lec.spring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Entity 에서 Embed 할 수 있는 클래스 임을 선언 --> Entity 안에서 선언이 가능해진다!
public class Address {
//    @Id
//    private Long id;

    // Embed 예제
    private String city;
    private String district;
    @Column(name = "address_detail")
    private String detail;
    private String zipCode;



}
