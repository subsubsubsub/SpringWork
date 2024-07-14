package com.lec.spring.beans;

import lombok.Data;

@Data
public class Score {
    int kor;
    int eng;
    int math;

    String comment;

    public Score(){
        System.out.println("Score() 생성");

    }
    public Score(int kor, int eng, int math, String comment) {
        super();
        System.out.printf("Score(%d, %d, %d, %s) 생성\n", kor, eng, math, comment);
        this.kor = kor;
        this.eng = eng;
        this.math = math;
        this.comment = comment;
    }
}

