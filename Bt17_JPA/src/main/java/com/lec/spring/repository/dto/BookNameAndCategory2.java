package com.lec.spring.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookNameAndCategory2 {     // 이건 Entity 아니에요
    private String name;
    private String category;
}
