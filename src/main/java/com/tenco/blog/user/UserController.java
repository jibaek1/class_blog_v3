package com.tenco.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor // DI 처리
@Controller
public class UserController {

    private final UserRepository userRepository;
    // httpSession <--- 세션 메모리에 접근을 할 수 있음
    private final HttpSession httpSession;

    // 주소 설계 :http://localhost:8080/user/update-form
    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request,HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        request.setAttribute("user", sessionUser);
        return "user/update-form";
    }

    // 회원 정보 수정 액션 처리
    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO reqDTO,HttpSession session,HttpServletRequest request) {


        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null) {
            return "redirect:/login-form";
        }

        //데이터 유효성 검사 처리
        reqDTO.validate();

        // 회원 정보 수정은 권한 체크가 필요 없다 (세션에서 정보를 가져오기 때문)
        User updateUser = userRepository.updateById(sessionUser.getId(),reqDTO);

        //세션 동기화
        session.setAttribute("sessionUser", updateUser);

        // 다시 회원 정보 보기 화면 요청
        return "redirect:/user/update-form";
    }

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


    /**
     * 로그인 화면 요청
     * @return
     */
    @GetMapping("/login-form")
    public String loginForm() {
        // 반환값이 뷰(파일) 이름이 됨(뷰 리졸버가 실제 파일 경로를 찾아 감)
        return "user/login-form";
    }

    // 로그인 액션 처리
    // 자원에 요청은 GET 방식이다. 단 로그인 요청은 예외
    // 보안상 이유

    // DTO 패턴 활용
    // 1. 입력 데이터 검증
    // 2. 사용자명과 비밀번호를 DB 접근해서 조회
    // 3. 로그인 성공/실패 처리
    // 4. 로그인 성공이라면 서버측 메모리에 사용자 정보를 저장
    // 5. 메인 화면으로 리다이렉트 처리
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO) {

        System.out.println("=== 로그인 시도 ===");
        System.out.println("사용자명 : " + loginDTO.getUsername());

        try {
            // 1.
            loginDTO.validate();
            // 2.
            User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(),
                    loginDTO.getPassword());
            // 3. 로그인 실패
            if (user == null) {
                // 로그인 실패 : 일치된 사용자 없음
                throw new IllegalArgumentException("사용자명 또는 비밀번호가 틀렸어");
            }

            // 4. 로그인 성공
            // principal은 접근 주체라는 의미를 가짐
            httpSession.setAttribute("sessionUser",user);

            // 5. 로그인 성공 후 리스트 페이지 이동
            return "redirect:/";

        } catch (Exception e) {
            // 필요하다면 에러메세지 생성해서 내려 보냄
            return "user/login-form";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        // 세션 무효화
        httpSession.invalidate();
        return "redirect:/";
    }



}
