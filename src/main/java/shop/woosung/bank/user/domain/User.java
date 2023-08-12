package shop.woosung.bank.user.domain;


import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.UserRole;

import java.time.LocalDateTime;

@Getter
public class User {

    private Long id;
    private String email;
    private String password;
    private String name;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String email, String password, String name, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User join(UserCreate userCreate, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(userCreate.getEmail())
                .password(passwordEncoder.encode(userCreate.getPassword()))
                .name(userCreate.getName())
                .role(userCreate.getRole())
                .build();
    }
}
