package com.lec.spring.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/*
API 응답을 만들기 위해 사용, 댓글을 가져오는 api 요청이 있을때,
댓글 목록 데이터를 JSON 형식으로 변환하여 응답하는 역할을 한다.
*/

@Data
@EqualsAndHashCode(callSuper=false) // 부모쪽은 호출하지 않겠다
@NoArgsConstructor
public class QryCommentList extends QryResult {

    @ToString.Exclude
    @JsonProperty("data")   // data 라는 이름으로 response
    List<Comment> list;
}
