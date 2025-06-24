package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // 생성자 자동 생성 + 멤버 변수 -> DI 처리 됨
@Repository // IoC + 싱글톤 패턴 관리 = 스프링 컨테이너
public class BoardRepository {

    // DI

    private final EntityManager em;

    /**
     * 게시글 저장 : User와 연관관계를 가진 Board 엔티티 영속화
     * @param board
     * @return
     */
    @Transactional
    public Board save(Board board) {
        // 비용속 상태의 Board Object를 영속성 컨텍스트에 저장하면
        em.persist(board);
        // 이 후시점에서는 사실 같은 메모리주소를 가르킨다.
        return board;
    }


    /**
     * 전체 게시글 조회
     */
    public List<Board> findByAll() {
        // JQPL - 쿼리 선택
        String jpql = "SELECT b FROM Board b ORDER By b.id DESC";
        TypedQuery query = em.createQuery(jpql,Board.class);
        List<Board> boardList = query.getResultList();
        return boardList;
    }

    /**
     *
     *
     * @param id : Board 엔티티에 ID 값
     * @return : Board 엔티티
     */
    public Board findById(Long id) {
        // 조회 - PK 조회는 무조건 엔티티 매니저 메서드 활용이 이득이다.
        Board board = em.find(Board.class,id);
        return board;
    }
}
