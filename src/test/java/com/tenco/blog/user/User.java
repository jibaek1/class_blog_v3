package com.tenco.blog.user;

/**
 * 빌더 패턴을 사용해서 User 클래스를 설계해 보자.
 */
public class User {

    private Integer id;
    private String username;
    private String password;
    private String email;

    // 빌드 패턴으로 설계해 보자.
    // 1. static 메서드를 활용해서 User 객체를 생성하는 메서드를 만든다.
    public static User builder() {
        return new User();
    }

    // 2. 각각의 멤버 메서드를 셋팅하는 메서드를 만들어 준다.
    public User id(Integer id) {
        this.id = id;
        return this;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
