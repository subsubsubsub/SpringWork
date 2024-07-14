
package com.lec.spring.domain.oauth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

    // 카카오에서 보내준 사용자의 정보를 자바 객체로 만들기 위한 클래스
@Data
public class KakaoProfile {
    public Long id;
    @JsonProperty("connected_at")
    public String connectedAt;
    public Properties properties;
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;


    @Data
    public static class KakaoAccount {

        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean profileNicknameNeedsAgreement;
        @JsonProperty("profile_image_needs_agreement")
        public Boolean profileImageNeedsAgreement;
        public Profile profile;

        @Data
        public static class Profile {
            public String nickname;
            @JsonProperty("thumbnail_image_url")
            public String thumbnailImageUrl;
            @JsonProperty("profile_image_url")
            public String profileImageUrl;
            @JsonProperty("is_default_image")
            public Boolean isDefaultImage;
            @JsonProperty("is_default_nickname")
            public Boolean isDefaultNickname;
        }   //  end Profile

    }   //  end KakaoAccount

    @Data
    public static class Properties {
        public String nickname;
        @JsonProperty("profile_image")
        public String profileImage;
        @JsonProperty("thumbnail_image")
        public String thumbnailImage;
    }

}   // end KakaoProfile