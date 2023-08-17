package shop.woosung.bank.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

import java.time.LocalDateTime;

@Getter
public class LoginResponseDto {
    private final Long id;
    private final String name;
    private final UserRole role;

    @Builder
    public LoginResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.role = user.getRole();
    }
}
