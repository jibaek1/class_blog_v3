package com.tenco.blog.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BoardPersistRepository.class)
@DataJpaTest
public class BoardPersistRepositoryTest {

    @Autowired
    private BoardPersistRepository br;

    @Test
    public void deleteById_test() {
        // given
        Long id = 1L;

        // when
        // 삭제할 게시글이 실제로 존재하는지 확인
        Board targetBoard = br.findById(id);
        Assertions.assertThat(targetBoard).isNotNull();

        // 영속성 컨텍스트에서 삭제 실행
        br.deleteById(id);

        // then
        List<Board> afterDeleteBoardList = br.findAll();
        Assertions.assertThat(afterDeleteBoardList.size()).isEqualTo(3);
    }

    @Test
    public void findAll_test() {
        // given 초기
        // db/data.sql (4개의 더미 데이터)

        // when 실행
        List<Board> boardList = br.findAll();

        // then
        System.out.println("size 테스트 : " +boardList.size());
        System.out.println("첫번째 게시글 제목 확인 : " + boardList.get(0).getTitle());

        // 네이티브 쿼리를 사용한다는 것은 영속성 컨텍스트를 우회 해서 일을 처리
        // JPQL 바로 영속성 켄텍스트를 우회하지만 조회된 이후에는 영속성 상태가 된다.

        for (Board board: boardList) {
            Assertions.assertThat(board.getId()).isNotNull();
        }

    }


    @Test
    public void save_test () {
        // given 기초설정
        Board board = new Board("제목555","내용555","승민군");

        // 저장 전 객체의 상태값 확인
        Assertions.assertThat(board.getId()).isNull();
        System.out.println("db에 저장 전 board : " + board);

        // when 실행
        // 영속성 컨텍스트를 통한 엔티티 저장
        Board savedBoard = br.save(board);

        // then 검증
        // 1. 저장 후에 자동 생성된 ID 확인
        Assertions.assertThat(savedBoard.getId()).isNotNull();
        Assertions.assertThat(savedBoard.getId()).isGreaterThan(0);

        // 2. 내용 일치 여부 확인
        Assertions.assertThat(savedBoard.getTitle()).isEqualTo("제목555");

        // 3. JPA가 자동으로 생성된 생성 시간 확인
        Assertions.assertThat(savedBoard.getCreatedAt()).isNotNull();

        // 4. 원본 객체 - board , 영속성 컨텍스트에 저장한 -saveBoard
        Assertions.assertThat(board).isSameAs(savedBoard);


    }


}
