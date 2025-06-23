package com.tenco.blog.board;

import com.tenco.blog.utils.MyDateUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
// 기본 생성자 - JPA 엔티티는 기본 생성자가 필요(기본 생성자를 자주 활용함)
@Data
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String username;

    // CreationTimestamp : 하이버네이트가 제공하는 어노테이션
    // 엔티티가 처음 저장할 때 현재 시간을 자동으로 설정한다.
    // pc -> db(날짜 주입)
    // v1에서는 SQL now()를 직접 사용했지만 JPA가 자동 처리
    @CreationTimestamp
    private Timestamp createdAt; // created_at () (스네이크 케이스로 자동 변환)

    // 생성자 만들어 주기
    public Board(String title, String content,String username) {
        this.title = title;
        this.content = content;
        this.username = username;
        // id와 createAt 은 JPA/Hibernate 가 자동으로 설정
    }

    // 머스태치에서 표현할 시간을 포맷기능을(행위) 스스로 만들자
    public String getTime() {
        return MyDateUtil.timestampFormat(createdAt);
    }

}
