package shop.woosung.bank.config.auth.dto;

import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginResponseDto {
    private Long id;
    private String username;
    private LocalDateTime createdAt;

    public LoginResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getName();
        this.createdAt = user.getCreatedAt();
    }
}
