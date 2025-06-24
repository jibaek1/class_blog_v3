package com.tenco.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor // DI 처리
@Controller
public class UserController {

    private final UserRepository userRepository;

    /**
     * 회원 가입 화면 요청
     * @return join-form.mustache
     */
    // 요청 되어 오는 주소 -> /join-form
    @GetMapping("/join-form")
    public  String joinForm() {
        return "user/join-form";
    }

    // 회원 가입 액션 처리
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest request) {

        System.out.println("==== 회원가입 요청 ====");
        System.out.println("사용자 명: " + joinDTO.getUsername());
        System.out.println("사용자 이메일: " + joinDTO.getEmail());

        try {
            // 1. 입력된 데이터 검증 (유효성 검사)
            joinDTO.validate();

            // 2. 사용자명 중복 체크
            User existUser = userRepository.findByUsername(joinDTO.getUsername());
            if(existUser != null) {
                throw new IllegalArgumentException("이미 존재하는 사용자명 입니다 "
                        + joinDTO.getUsername());
            }

            // 3. DTO 를 User Object 변환
            User user = joinDTO.toEntity();

            // 4. User Object를 영속화 처리
            userRepository.save(user);

            //PRG 패턴 처리
            return "redirect:/login-form";
        } catch (Exception e) {
            // 검증 실패 시 보통 에러 메세지와 함께 다시 폼에 전달
            request.setAttribute("errorMessage", "잘못된 요청이야");
            return "user/join-form";
        }
    }


    @GetMapping("/login-form")
    public String loginForm() {
        // 반환값이 뷰(파일) 이름이 됨(뷰 리졸버가 실제 파일 경로를 찾아 감)
        return "user/login-form";
    }

    // 주소 설계 :http://localhost:8080/user/update-form
    @GetMapping("/user/update-form")
    public String updateForm() {
        return "user/update-form";
    }

    @GetMapping("/logout")
    public String logout() {
        // "redirect: " 스프링 에서 접두사를 사용하면 다른 URL로 리다이렉트 됨
        // 즉 리다이렉트 한다는 것은 뷰를 렌더링 하지 않고 브라우저가 재 요청을
        // 다시 하게끔 유도 한다.
        return "redirect:/";
    }



}
