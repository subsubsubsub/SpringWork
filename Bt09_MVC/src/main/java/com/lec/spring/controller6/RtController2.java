package com.lec.spring.controller6;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 국립국어원 표준 국어대사전 API : https://stdict.korean.go.kr/openapi/openApiInfo.do
 */
@Controller
public class RtController2 {
    @Value("${app.api-key.korean-stddic}")
    private String korean_stddic_key;

    @RequestMapping("page/test40")
    public void test40() {
    }  // CORS 때문에 client 에서 직접 요청 불가

    @RequestMapping("page/test41")
    public void test41() {
    }  // Server to Server 통신으로 가져오기

        // 이렇게 하셔야 합니다
    @RequestMapping("api/test50")
    @ResponseBody
    public ResponseEntity<String> test50(@RequestParam(defaultValue = "나무") String q) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://stdict.korean.go.kr/api/search.do")
                .queryParam("key", korean_stddic_key)
                .queryParam("q", q)
                .queryParam("req_type", "json")
                .build()
                .encode()
                .toUri();

        return new RestTemplate().getForEntity(uri, String.class);

    }
}
