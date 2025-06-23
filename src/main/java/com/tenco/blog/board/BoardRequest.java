package com.tenco.blog.board;

import lombok.Data;

/**
 * 클라이언트에게 넘어온 데이터를
 * Object로 변화해서 전달하는 DTO 역할을 담당한다
 */
public class BoardRequest {

    // 정적 내부 클래스로 기능별로 DTO 관리
    // 게시글 저장 요청 데이터
    // BoardRequest.SaveDTO 변수명
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        private String username;


        // DTO에서 Entity로 변환하는 메서드 만들기
        // 계층간 데이터 변환을 명확하게 분리 하기 위함
        public Board toEntity() {
            return new Board(title, content, username);
        }

    } // end of SaveDTO


    // 게시글 수정용 DTO 추가
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private String username;

        // 검증 메서드 (유효성 검사 기능을 추가)
        public void validate() throws IllegalAccessException {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalAccessException("제목은 필수 입니다");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalAccessException("내용은 필수 입니다");
            }
        }

    }

}
